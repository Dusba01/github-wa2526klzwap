# syntax=docker/dockerfile:1

############################################
# Stage 1 — build the WAR with Maven
############################################
FROM maven:3.9-eclipse-temurin-11 AS build

WORKDIR /app

# Cache dependencies first (only re-downloads when pom.xml changes)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# CACHEBUST arg: pass a unique value (e.g. timestamp) at build time to force
# Docker to invalidate the cache from this point onwards, ensuring the latest
# source code is always compiled instead of reusing a stale cached layer.
# Usage: docker compose build --build-arg CACHEBUST=$(date +%s) tomcat
ARG CACHEBUST=1

# Build the application
COPY src ./src
RUN mvn -B -q clean package

############################################
# Stage 2 — install Apache Tomcat explicitly
# on a plain Temurin JDK 11 base image.
# Tomcat 10.1.x serves the Jakarta EE 9+ namespace used by this app.
############################################
FROM eclipse-temurin:11-jdk-jammy

# --- Tomcat version (pinned) -------------------------------------------------
ENV TOMCAT_MAJOR=10
ENV TOMCAT_VERSION=10.1.34
ENV CATALINA_HOME=/opt/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH

# --- Download, verify and install Tomcat ------------------------------------
# The matching .sha512 file is fetched from the Apache archive and used to
# verify the tarball, so the build fails loudly on any corrupted/tampered
# download instead of relying on a hand-copied hash.
RUN set -eux; \
    apt-get update; \
    apt-get install -y --no-install-recommends curl ca-certificates; \
    rm -rf /var/lib/apt/lists/*; \
    mkdir -p "$CATALINA_HOME"; \
    base="https://archive.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR}/v${TOMCAT_VERSION}/bin"; \
    curl -fSL "${base}/apache-tomcat-${TOMCAT_VERSION}.tar.gz" -o /tmp/tomcat.tar.gz; \
    curl -fSL "${base}/apache-tomcat-${TOMCAT_VERSION}.tar.gz.sha512" -o /tmp/tomcat.tar.gz.sha512; \
    echo "$(awk '{print $1}' /tmp/tomcat.tar.gz.sha512)  /tmp/tomcat.tar.gz" | sha512sum -c -; \
    tar -xf /tmp/tomcat.tar.gz -C "$CATALINA_HOME" --strip-components=1; \
    rm -f /tmp/tomcat.tar.gz /tmp/tomcat.tar.gz.sha512; \
    rm -rf "$CATALINA_HOME"/webapps/*; \
    # Let the HTTP connector swallow (drain) the remainder of an aborted upload
    # body instead of resetting the TCP connection. Without this, an over-limit
    # upload makes the browser show ERR_CONNECTION_RESET; with it, Tomcat reads
    # the leftover bytes so the servlet's "file too large" redirect is delivered
    # and the user sees the friendly error banner.
    sed -i 's#protocol="HTTP/1.1"#protocol="HTTP/1.1" maxSwallowSize="-1"#' \
        "$CATALINA_HOME/conf/server.xml"

# --- Run as a non-root user --------------------------------------------------
RUN groupadd -r tomcat && useradd -r -g tomcat -d "$CATALINA_HOME" tomcat; \
    chown -R tomcat:tomcat "$CATALINA_HOME"

# --- Deploy the built WAR as ROOT so the app is served at "/" ---------------
COPY --from=build --chown=tomcat:tomcat \
     /app/target/lecture-notes-app.war "$CATALINA_HOME/webapps/ROOT.war"

USER tomcat
WORKDIR $CATALINA_HOME

EXPOSE 8080

CMD ["catalina.sh", "run"]

# Run The App With Docker

This guide explains how to run the Lecture Notes App using the provided `Dockerfile` and `compose.yaml`.

## What Docker Runs

The Compose stack starts four services:

| Service | Purpose |
|---|---|
| `tomcat` | Builds the Java WAR and runs it on Apache Tomcat 10.1 |
| `db` | Runs PostgreSQL 16 |
| `minio` | Runs MinIO object storage for uploaded PDFs |
| `minio-init` | Creates the MinIO bucket before Tomcat starts |

The app is deployed as `ROOT.war`, so it is available directly at:

```text
http://localhost:8080
```

## Requirements

Install:

```text
Docker
Docker Compose
```

You do not need to install Java, Maven, Tomcat, PostgreSQL, or MinIO locally. The Docker image builds and runs everything needed.

## Start The App

Run this from the project root, where `compose.yaml` is located:

```bash
docker compose up --build
```

After startup, open:

```text
http://localhost:8080
```

The MinIO web console is available at:

```text
http://localhost:9001
```

Default MinIO login:

```text
username: minioadmin
password: minioadmin
```

## Run In The Background

Use detached mode:

```bash
docker compose up -d --build
```

Check container status:

```bash
docker compose ps
```

## Stop The App

Stop containers but keep database and MinIO data:

```bash
docker compose down
```

Stop containers and delete persisted data:

```bash
docker compose down -v
```

Use `docker compose down -v` only when you want a fresh database and empty object storage.

## Ports

Default host ports:

| Service | URL / Port |
|---|---|
| App | `http://localhost:8080` |
| PostgreSQL | `localhost:5433` |
| MinIO API | `http://localhost:9000` |
| MinIO Console | `http://localhost:9001` |

PostgreSQL uses host port `5433` to avoid conflicts with a local PostgreSQL running on `5432`.

## Optional Environment File

The app works without a `.env` file because `compose.yaml` provides defaults.

To override defaults, create a `.env` file in the project root:

```bash
touch .env
```

Example `.env`:

```dotenv
POSTGRES_DB=lecturenotes
POSTGRES_USER=lecturenotes
POSTGRES_PASSWORD=lecturenotes

MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin
S3_BUCKET_NAME=university-notes-app
S3_REGION=us-east-1

APP_PORT=8080
POSTGRES_PORT=5433
MINIO_API_PORT=9000
MINIO_CONSOLE_PORT=9001
```

Do not commit `.env` if it contains real credentials.

## Database Initialization

The PostgreSQL container runs these scripts automatically on first startup:

```text
src/main/database/schema.sql
src/main/database/insert.sql
```

They are mounted into the container as:

```text
/docker-entrypoint-initdb.d/01-schema.sql
/docker-entrypoint-initdb.d/02-insert.sql
```

These scripts run only when the PostgreSQL volume is created for the first time.

If you change the schema and want Docker to re-run the SQL scripts:

```bash
docker compose down -v
docker compose up --build
```

This deletes the current PostgreSQL data and MinIO uploaded files.

## Object Storage For PDFs

Uploaded PDFs are stored in MinIO.

Inside Docker, Tomcat connects to MinIO using:

```text
S3_ENDPOINT=http://minio:9000
```

This is correct because `minio` is the Compose service name. Do not use `localhost` inside the Tomcat container.

The bucket name defaults to:

```text
university-notes-app
```

The `minio-init` service creates the bucket automatically before Tomcat starts.

Uploaded PDFs are stored under:

```text
uploads/
```

## Build Details

The `Dockerfile` uses a two-stage build.

Stage 1 builds the WAR with Maven:

```text
maven:3.9-eclipse-temurin-11
```

Stage 2 installs and runs Tomcat:

```text
eclipse-temurin:11-jdk-jammy
Apache Tomcat 10.1.34
```

The built WAR is copied to:

```text
/opt/tomcat/webapps/ROOT.war
```

That is why the app is served at the root URL:

```text
http://localhost:8080
```

## Useful Commands

View all logs:

```bash
docker compose logs
```

View Tomcat logs:

```bash
docker compose logs tomcat
```

View PostgreSQL logs:

```bash
docker compose logs db
```

View MinIO logs:

```bash
docker compose logs minio minio-init
```

Rebuild only the Tomcat image:

```bash
docker compose build tomcat
docker compose up -d tomcat
```

Force a no-cache rebuild:

```bash
docker compose build --no-cache tomcat
docker compose up -d tomcat
```

Open a shell in the Tomcat container:

```bash
docker compose exec tomcat sh
```

Open a shell in the PostgreSQL container:

```bash
docker compose exec db sh
```

Open a shell in the MinIO container:

```bash
docker compose exec minio sh
```

## Verify PDF Uploads In MinIO

Use the MinIO console:

```text
http://localhost:9001
```

Open the bucket:

```text
university-notes-app
```

Uploaded files should appear under:

```text
uploads/
```

You can also list objects from the terminal:

```bash
docker compose exec minio sh -c 'mc alias set local http://localhost:9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD" && mc ls local/university-notes-app/uploads/'
```

## Troubleshooting

If the app does not start, check service status:

```bash
docker compose ps
```

If Tomcat fails, inspect logs:

```bash
docker compose logs tomcat
```

If PostgreSQL data is missing after changing SQL scripts, recreate volumes:

```bash
docker compose down -v
docker compose up --build
```

If PDF upload times out, rebuild Tomcat and check MinIO:

```bash
docker compose up -d --build tomcat
docker compose logs minio minio-init
docker compose exec tomcat env
```

Expected Tomcat environment values:

```text
S3_ENDPOINT=http://minio:9000
S3_BUCKET_NAME=university-notes-app
AWS_EC2_METADATA_DISABLED=true
```

If a port is already in use, set a different host port in `.env`, for example:

```dotenv
APP_PORT=8081
POSTGRES_PORT=5434
MINIO_API_PORT=9100
MINIO_CONSOLE_PORT=9101
```

Then restart:

```bash
docker compose up -d --build
```

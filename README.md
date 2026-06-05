# Lecture Notes App

A web application for university students to upload, search, and manage course notes. Users can register, upload PDF documents tied to specific courses, rate and favourite notes shared by others, and manage their own profile.


## Group members

| Name    | Surname    | ID      | Email                               |
|:--------|:-----------|:--------|:------------------------------------|
| Edoardo | Zanella    | 2195316 | edoardo.zanella.2@studenti.unipd.it |
| Laszlo  | Kosa       | 2183964 | laszlo.kosa@studenti.unipd.it       |
| Luca    | Dusi       | 2200074 | luca.dusi@studenti.unipd.it         |
| Milos   | Trifunovic | 2183388 | milos.trifunovic@studenti.unipd.it  |

---

## Technologies

| Layer | Technology |
|---|---|
| Backend | Java 11, Jakarta Servlets, JSTL |
| Frontend | JSP, CSS3, JavaScript |
| Database | PostgreSQL 16 |
| Object Storage | MinIO (S3-compatible) |
| Application Server | Apache Tomcat 10.1 |
| Build | Maven 3 |
| Containerisation | Docker, Docker Compose |

---

## Deployed application (remote)

The app is also deployed and available at `https://peernotes.space/`.

---

## Running with Docker (local)

The easiest way to run the app is with Docker — no need to install Java, Maven, Tomcat, PostgreSQL, or MinIO locally.

```bash
docker compose up --build
```

The app will be available at `http://localhost:8080`.

---

## Note on seed data

To provide a realistic browsing experience on first run, the database is pre-populated
with sample notes via `src/main/database/insert.sql`. These seed records exist only as
metadata: they describe a note (title, course, author, and so on) but have no
corresponding file in object storage. As a result, **the seed notes cannot be
downloaded** — attempting to do so will fail because there is no underlying object to
serve. Only notes that are uploaded through the application have a real file backing them
and are therefore fully downloadable.

---

## Project structure

```
lecture-notes-app/
│
├── src/main/java/
│   ├── controller/           ← Servlets (User, Note, Course, Favorite, Rating)
│   │   └── AbstractDatabaseServlet.java  ← shared JNDI DataSource lookup
│   ├── dao/                  ← One class per query (note/, user/, course/, favorite/, rating/)
│   │   ├── AbstractDAO.java
│   │   └── DataAccessObject.java
│   ├── filter/               ← AuthenticationFilter, CharacterEncodingFilter
│   ├── model/                ← User, Note, Course, CourseSummary, Favorite, Rating
│   └── utils/
│       └── StorageService.java  ← S3 singleton (MinIO / R2)
│
├── src/main/webapp/
│   ├── META-INF/context.xml  ← JNDI DataSource (DB connection pool)
│   ├── WEB-INF/web.xml       ← Servlet mappings, filters, resource-ref
│   ├── jsp/                  ← login, register, home, upload, profile, favorites
│   ├── css/                  ← base, layout, components, auth, home, profile
│   └── js/                   ← notes.js, upload.js, sidebar.js
│
├── src/main/database/
│   ├── schema.sql
│   └── insert.sql
│
├── Dockerfile
├── compose.yaml
└── pom.xml
```

# klzwap - Web Applications (WA) Repository

## Group members:

| Name    | Surname     | ID      | Email                               |
|:--------|:------------|:--------|:------------------------------------|
| Edoardo | Zanella     | 2195316 | edoardo.zanella.2@studenti.unipd.it |
| Laszlo  | Kosa        | 2183964 | laszlo.kosa@studenti.unipd.it       |
| Luca    | Dusi        | 2200074 | luca.dusi@studenti.unipd.it         |
| Milos   | Trifunovic  | 2183388 | milos.trifunovic@studenti.unipd.it  |


## REQUIREMENTS

- Java JDK 11 (or higher)
- Apache Tomcat 9 (or higher)
- Maven 3.x
- PostgreSQL 

## ENVIRONMENT CONFIGURATION

The application requires environment variables to connect to:
- PostgreSQL database
- Cloud storage service (R2 compatible)

1. Create a `.env` file in src/main/resources
2. Copy the content from `.env.example`
3. Fill in your local or remote credentials

## DATABASE SETUP
The project uses PostgreSQL.

1. Create the database:
CREATE DATABASE lecturenotes;

2. Execute the SQL scripts in the following order:
   - `schema.sql` (creates tables and constraints)
   - `insert.sql` (loads sample data)

## STORAGE CONFIGURATION
The project uses an S3-compatible storage service (e.g. Cloudflare R2).

If storage is not configured:
- file upload/download features will be disabled

## PROJECT STRUCTURE

lecture-notes-app/
│
├── src/main/java/
│   ├── controller/       ← Servlets
│   ├── dao/              ← Database logic
│   ├── model/            ← (User, Note...)
│   ├── database/         ← SQL queries
│   └── utils/            ← Utility (DB connection, StorageService)
│
├── src/main/webapp/
│   ├── jsp/
│   ├── js/
│   │   └── notes.js       ← AJAX
│   │
│   └── WEB-INF/
│       └── web.xml
│
└── pom.xml 


## OPEN LOGIN PAGE

1. **Import the project** in IntelliJ IDEA (or Eclipse) as a **Maven Project**.

2. **Check the `pom.xml` file**
   - Make sure the packaging is `war`
   - Ensure all dependencies are downloaded (Servlet API, JSP, MySQL driver, JSON)

3. **Configure Apache Tomcat**
   - Download Apache Tomcat 9/10
   - Configure Tomcat in IntelliJ (Run → Edit Configurations → Tomcat Server → Local)
   - Add the `war exploded` artifact as deployment

4. **Start Tomcat**
   - Click **Run**
   - Wait for the message `Tomcat started on port 8080`

5. **Open the browser**
   - Go to: `http://localhost:8080/lecture-notes-app/`
   - The **login page** (`login.jsp`) will open



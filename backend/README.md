# IBAN

Java 21 Spring Boot application built with Maven.

Run all commands below from the `backend/` directory.

## Run the backend

Start PostgreSQL using the Compose file in the project root (from `backend/`):

```sh
docker compose -f ../docker-compose-database.yml up -d
```

The backend connects to `jdbc:postgresql://localhost:5433/iban` with
username `dev_user` and password `dev_user`. Override these defaults using
`DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables.

Flyway applies migrations from `src/main/resources/db/migration` automatically
on startup. The initial migration creates the `iban` and `bankcode` tables
in the default PostgreSQL schema.
Flyway tracks applied migrations in `public.flyway_schema_history`.
Add subsequent changes as `V2__description.sql`, `V3__description.sql`, and so on;
leave applied migrations unchanged. PostgreSQL must be running for application
startup. 

Install JDK 21 or newer and set `JAVA_HOME` to its installation directory.
The Maven wrapper downloads Maven automatically on first use (internet access required).

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

macOS / Linux:

```sh
sh mvnw spring-boot:run
```

The server listens on port 8080. Check startup at
<http://localhost:8080/actuator/health>, which returns JSON with `"status":"UP"`.


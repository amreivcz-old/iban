# IBAN

The Java Spring Boot backend lives in [`backend/`](backend/README.md).
The React + TypeScript frontend uses Vite and lives in [`frontend/`](frontend/README.md).

## Start the backend

With JDK 21 or newer installed and `JAVA_HOME` configured:

```powershell
docker compose -f docker-compose-database.yml up -d
cd backend
.\mvnw.cmd spring-boot:run
```

On macOS / Linux, use `sh mvnw spring-boot:run` from `backend/`.
Check <http://localhost:8080/actuator/health> after startup.
See the [backend README](backend/README.md) for testing, packaging, and certificate setup.

In IntelliJ IDEA, import `backend/pom.xml` as a Maven project and run
`backend/src/main/java/com/iban`.

## Start the frontend

From the project root:

```sh
cd frontend
npm install
npm run dev
```

# Kanboom

A small Kanban board API. Three columns — Todo, In Progress, Done — nothing fancier than that.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-blue)

## What's here

- Register/login with JWT auth (stateless, no sessions)
- Tasks scoped to whoever owns them — you only ever see your own
- Move a task between Todo / In Progress / Done
- Postgres via NeonDB, JPA/Hibernate for persistence
- Layered the usual way: controller, service, repository, entity

## Running it locally

You'll need a Postgres database (Neon or otherwise) and Java 17.

Create `src/main/resources/application-local.properties` (already gitignored) with:

```
DB_URL=jdbc:postgresql://<host>:5432/<db>?sslmode=require
DB_USERNAME=<user>
DB_PASSWORD=<password>
JWT_SECRET=<a long random base64 string>
```

Then:

```
./mvnw spring-boot:run
```

The app starts on `localhost:8080` with the `local` profile active by default.

## API

| Method | Path                     | Auth | Description              |
|--------|--------------------------|------|---------------------------|
| POST   | `/api/auth/register`     | no   | create an account         |
| POST   | `/api/auth/login`        | no   | get a JWT                 |
| GET    | `/api/tasks`             | yes  | list your tasks (optional `?status=`) |
| POST   | `/api/tasks`             | yes  | create a task              |
| GET    | `/api/tasks/{id}`        | yes  | get one task               |
| PUT    | `/api/tasks/{id}`        | yes  | update a task              |
| PATCH  | `/api/tasks/{id}/status` | yes  | move it between columns    |
| DELETE | `/api/tasks/{id}`        | yes  | delete a task              |

Send the JWT from login/register as `Authorization: Bearer <token>` on everything else.

## Status

Backend is functional. Frontend, CI, and deployment (Render + Vercel) are next.

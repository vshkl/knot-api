# Knot API

A playground for learning backend development using Kotlin and Ktor.

## What's here

- Sign-up & Sign-in with email and password.
- Authentication with JWT token and token refresh.
- Basic CRUD operations for notes.
- Cursor-based pagination for notes.

## What's not

- Token tracking.
- Email confirmation flow.
- Password reset flow.

## Endpoints
- `POST`    `/auth/sign-up`
- `POST`    `/auth/sign-in`
- `POST`    `/auth/refresh-token`
- `GET`     `/user`
- `GET`     `/notes?limit=&before=&after=&including=`
- `POST`    `/notes`
- `GET`     `/notes/:id`
- `PATCH`   `/notes/:id`
- `DELETE`  `/notes/:id`

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep Changelog](https://keepachangelog.com)

## [0.3.0] - 2026-08-23

### Added
- **Domain Entities:** Add `Chat`, `DirectChat` and `ChatMember` entities.
- **Database Migrations**: Add migrations for domain entities.
- **New API**: add an endpoint for creating chats.
  - `POST /api/v1/chats/direct` - gets or create a chat

## [0.2.0] - 2026-08-18

### Added
- **New API**: Add endpoint for working with users.
  - `GET /api/v1/users/search?query` - gets users by query
  - `GET /api/v1/users/me` - gets a user profile
  - `PATCH /api/v1/users/me/username` - updates a user's username
  - `PATCH /api/v1/users/me/name` - updates a user's display name
  - `PATCH /api/v1/users/me/birthdate` - updates a user's birthdate

## [0.1.0] - 2026-08-13

### Added
- **Domain Entities**: Created basic `User` and `RefreshToken` entities and repositories for it.
- **Database Migrations**: Integrated Liquibase for migrations management. Add basic migrations for `User` and `RefreshToken` entities.
- **Authentication**: Implemented registration, authentication and refresh token endpoints.
  - `POST /api/v1/auth/register`
  - `POST /api/v1/auth/login`
  - `POST /api/v1/auth/refresh`

### Security
- **Authorization**: Implemented a `JwtAuthorizationFilter` for access token verification.
- **Password hashing**: Implemented hashing of user passwords using the Argon2 algorithm.

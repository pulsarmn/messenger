# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep Changelog](https://keepachangelog.com)

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

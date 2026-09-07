# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep Changelog](https://keepachangelog.com)

## [0.5.0] - 2026-09-07

### Added
- **Remove API**: Remove the `SEND /chats/{chatId}/send-message` STOMP endpoint.
- **New API**: Add an HTTP REST endpoints to manipulate messages.
  - `GET /api/v1/chats/{chatId}/messages` - gets messages by cursor.
  - `POST /api/v1/chats/{chatId}/messages` - creates a new message.
  - `PATCH /api/v1/messages/{id}` - updates a message content.
  - `DELETE /api/v1/messages/{id}` - deletes a message.

## [0.4.0] - 2026-09-05

### Added
- **Domain Entities**: Add the `Message` domain entity.
- **Database Migrations**: Add a migrations for `Message` entity.
- **New API**: Add a STOMP endpoint to send messages in real-time.
  - `SEND /chats/{chatId}/send-message` - creates a new message.

### Security
- **Authorization**: Add the `JwtAuthorizationChannelInterceptor` for verification access tokens in STOMP frames.

## [0.3.0] - 2026-08-23

### Added
- **Domain Entities:** Add `Chat`, `DirectChat` and `ChatMember` entities.
- **Database Migrations**: Add migrations for domain entities.
- **New API**: Add an endpoint for creating chats.
  - `POST /api/v1/chats/direct` - gets or create a chat

## [0.2.0] - 2026-08-18

### Added
- **New API**: Add endpoint for working with users.
  - `GET /api/v1/users/search?query` - gets users by query.
  - `GET /api/v1/users/me` - gets a user profile.
  - `PATCH /api/v1/users/me/username` - updates a user's username.
  - `PATCH /api/v1/users/me/name` - updates a user's display name.
  - `PATCH /api/v1/users/me/birthdate` - updates a user's birthdate.

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

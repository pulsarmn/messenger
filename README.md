# Messenger Service

Backend service for real-time messaging.

This project is being creating to demonstrate my programming skills and because I love it.

## Technology Stack

- **Java 25**
- **Spring Boot**
  - **Spring Core**
  - **Spring Web/WebMVC**
  - **Spring Data JPA**
  - **Spring Security**
  - **Spring Liquibase**
  - **Spring Validation**
  - **Spring Test**
- **Liquibase**
- **PostgreSQL**
- **Docker/Docker Compose**
- **Gradle**
- **JUnit 5**
- **Mockito**
- **AssertJ**

## Quick Launch

### Requirements
- **Java 25+**
- **Docker/Docker Compose**

### 1. Cloning the repository

```bash
git clone https://github.com/pulsarmn/messenger

cd messenger
```

### 2. Launching the database

```bash
docker compose -f compose.dev.yaml up -d
```

### 3. Launching the application

```bash
./gradlew bootRun
```

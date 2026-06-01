# Bookstore REST API

A simple Spring Boot REST API for managing books — built to learn Maven fundamentals.

## Tech Stack

- **Java 25**, **Spring Boot 4.1.0-RC1**
- **Maven** (profiles, resource filtering, multi-environment)
- **JPA / Hibernate** with **H2** (dev) or **PostgreSQL** (prod)
- **Jakarta Validation** for request DTO validation
- **JUnit 5 + Mockito + MockMvc** for tests

## Prerequisites

- JDK 25+
- Maven 3.9+
- PostgreSQL (prod profile only)

## Quick Start (Dev)

```bash
cd bookstore
mvn spring-boot:run
```

Runs on `http://localhost:8080` with an in-memory H2 database.

## Profiles

| Profile | Database | Activation |
|---------|----------|------------|
| `dev` (default) | H2 (in-memory) | auto |
| `prod` | PostgreSQL | `mvn spring-boot:run -P prod` |

### Production Setup

1. Create a PostgreSQL database and user.
2. Copy `application-prod.properties.example` to `application-prod.properties` and fill in your credentials (file is `.gitignore`d).
3. Set the environment variable `POSTGRES_PASSWORD`.

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/books` | Create a book |
| GET | `/api/books` | List all books |
| GET | `/api/books/{id}` | Get by ID |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |

All `POST`/`PUT` bodies must include `title`, `author`, `isbn`, `price`, and `publishedDate`.

## Project Structure

```
bookstore/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/deba/bookstore/
    │   │   ├── BookstoreApplication.java
    │   │   ├── controller/BookController.java
    │   │   ├── dto/     (CreateBookRequest, BookResponse)
    │   │   ├── entity/  Book.java
    │   │   ├── exception/ (BookNotFoundException, GlobalExceptionHandler)
    │   │   ├── repository/BookRepository.java
    │   │   └── service/ BookService.java
    │   └── resources/
    │       ├── application.properties
    │       ├── application-dev.properties
    │       └── application-prod.properties.example
    └── test/
        └── java/com/deba/bookstore/
            ├── controller/BookControllerTest.java
            └── service/BookServiceTest.java
```

## Maven Commands

| Command | Purpose |
|---------|---------|
| `mvn compile` | Compile source code |
| `mvn test` | Run tests |
| `mvn package` | Build JAR |
| `mvn spring-boot:run` | Run application |
| `mvn spring-boot:run -P prod` | Run with PostgreSQL |
| `mvn clean` | Delete `target/` |

# Bulletin Board API (Spring Boot 3)

Simple bulletin board REST API for CRUD operations on posts.

## Tech Stack
- Java 17
- Spring Boot 3
- Gradle
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- JUnit 5
- MockMvc

## Project Directory Structure

```text
.
├── build.gradle
├── settings.gradle
├── src
│   ├── main
│   │   ├── java/com/example/bulletin
│   │   │   ├── BulletinBoardApplication.java
│   │   │   ├── controller/PostController.java
│   │   │   ├── dto
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── PostCreateRequest.java
│   │   │   │   ├── PostUpdateRequest.java
│   │   │   │   └── PostResponse.java
│   │   │   ├── entity/Post.java
│   │   │   ├── exception
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── PostNotFoundException.java
│   │   │   ├── repository/PostRepository.java
│   │   │   └── service/PostService.java
│   │   └── resources/application.yml
│   └── test
│       └── java/com/example/bulletin/controller/PostControllerIntegrationTest.java
└── README.md
```

## Design Decisions
- **Layered architecture**: `controller` -> `service` -> `repository`.
- **DTO separation**: request/response DTOs isolate API from entity.
- **Validation**: `jakarta.validation` annotations on DTOs.
- **Consistent response format**:
  - Success: `{ "success": true, "data": ... , "error": null }`
  - Failure: `{ "success": false, "data": null, "error": { "code", "message", "timestamp" } }`
- **Exception handling**: global handler maps domain/validation/unexpected errors to proper status codes.
- **Pagination**: list API uses Spring `Pageable`.

## How to Run

```bash
./gradlew bootRun
```

H2 console:
- http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:bulletin`
- Username: `sa`
- Password: *(empty)*

## Run Tests

```bash
./gradlew test
```

## Example cURL Requests

### 1) Create Post
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"title":"Hello","content":"First post","author":"alice"}'
```

### 2) Get Post List (pagination)
```bash
curl "http://localhost:8080/api/posts?page=0&size=10"
```

### 3) Get Post Detail
```bash
curl http://localhost:8080/api/posts/1
```

### 4) Update Post
```bash
curl -X PUT http://localhost:8080/api/posts/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated","content":"Updated content","author":"bob"}'
```

### 5) Delete Post
```bash
curl -X DELETE http://localhost:8080/api/posts/1
```

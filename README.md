
# Notes API
A secure, scalable RESTful API enabling users to create, read, update and delete notes, with sharing and search capabilities.

# Caracteristics

* Creating, reading, updating and deleting notes
* Share notes between users
* Keyword-based note search with text indexing
* JWT-based authentication
* Rate limiting and overload protection
* Documented with Swagger/OpenAPI
* Unit and integration testing

# Technologies and Justification
* **Java 21** - Programming language
* **Spring Boot 3.3** - Framework
* **Spring Security** - For authentication and authorization
* **Spring Data JPA** - For data persistence
* **PostgreSQL** - Databases
* **Flyway** - Database migration management
* **JWT** - For stateless authentication
* **Resilience4j** - For rate limiting and resilience
* **Hibernate Search** - For text indexing and search
* **Swagger/OpenAPI** - For API documentation
* **JUnit 5 & Mockito** - For testing
* **Docker & Docker Compose**
* **Maven** - For dependency management and build

# Architecture
The application follows a layered architecture:
* ├── config/           # Spring Boot configuration and security
* ├── controller/       # REST controllers exposing endpoints
* ├── dto/              # Data transfer objects
* ├── exception/        # Custom exception handling
* ├── entity/            # JPA entities
* ├── repository/       # Data access with Spring Data JPA
* ├── service/          # Business Logic

# Prerequisites
* JDK 21
* Maven 3.8+
* Docker & Docker Compose (to run PostgreSQL)
* Postman (to test the API) or web browser (for Swagger UI)

# Installation and startup
## Option 1: With Docker Compose (recommended)

1. Clone the repository:

`git clone https://github.com/edmichs/notes-back.git
cd notes-back`

2. Launch the application with Docker Compose:

`docker-compose up`
This command starts PostgreSQL and the application in separate containers.

## Option 2: Manual installation

1. Clone the repository:

`git clone https://github.com/edmichs/notes-back.git
cd notes-back`

2. Set up a PostgreSQL database:

* Install PostgreSQL
* Create a database: CREATE DATABASE notes;
* Create a user: CREATE USER admin WITH PASSWORD 'admin';
* Grant privileges: GRANT ALL PRIVILEGES ON DATABASE notes TO admin;

3. Update  `src/main/resources/application.yml` with your connection parameters.

4. Run the application:

   `mvn spring-boot:run`

# API Endpoints
## Authentication

* **POST /api/auth/signup**: Creating a user account

`{
    "username" : "test2",
    "email" : "test2@gmail.com",
    "password" : "test123",
    "roles": ["user"]
}`

* **POST /api/auth/login**: Login and obtain a JWT token

`{
  "username": "test1" ,
  "password": "test123"
}`

Response:

`{
  "token":"eyJhbGciOiJIUzI1NiJ9....",
  "refreshToken":"eyJhbGciOiJIUzI1NiJ9...",
  "type":"Bearer",
  "id":3,
  "username":"test1",
  "email":"test1@gmail.com",
  "roles":["user"]
}`

## Notes
All the following endpoints require a JWT authentication header:

`Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...`

* **GET /api/notes:** Retrieve all user notes
* **GET /api/notes/{id}**: Retrieve a note by ID
* **POST /api/notes**: Create a new note

`{
  "title" : "Test test",
  "content" : "Test content"
}`

* **PUT /api/notes/{id}**: Update an existing note

`{
    "title" : "Test updated",
    "content" : "Test content updated"
}`

* **DELETE /api/notes/{id}**: Delete a note 
* **POST /api/notes/{id}/share**: Share a note with another user

`{
  "username" : "testShared"
}`

* **GET /api/search?q=test**: Search notes by keywords

# Traffic management

The API implements flow limitation mechanisms with **Resilience4j**:
These limits can be adjusted in the application.yml.

# Tests
## Test Execution
To run all tests (unit and integration):

`mvn test`

To run unit tests only:

`mvn test -Dgroups="UnitTest"`

To run integration tests only:

`mvn test -Dgroups="IntegrationTest"`


# API Documentation:
Detailed Swagger documentation is available at the following address:

`http://localhost:8080/swagger-ui/index.html`

A Postman collection is also available at the project root.

[Speer-Technologies-Notes.postman_collection](Speer-Technologies-Notes.postman_collection)

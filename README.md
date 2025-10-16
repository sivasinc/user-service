# User Service - Microservice

User Management Microservice built with Spring Boot 3.5.6 and Java 21.

## Technologies

- **Java:** 21
- **Spring Boot:** 3.5.6
- **Database:** PostgreSQL 15
- **Build Tool:** Maven 3.9.11
- **Testing:** JUnit 5, Testcontainers
- **Code Coverage:** JaCoCo
- **Code Quality:** SonarCloud
- **Security Scanning:** OWASP Dependency Check, Trivy
- **CI/CD:** GitHub Actions

## Features

- ✅ RESTful API for user management
- ✅ Spring Security integration
- ✅ PostgreSQL database
- ✅ Comprehensive unit and integration tests
- ✅ Code coverage reporting (JaCoCo)
- ✅ Automated CI/CD pipeline
- ✅ Security vulnerability scanning
- ✅ Code quality analysis (SonarCloud)

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (for running PostgreSQL)
- Git

## Local Development

### 1. Clone the repository
```bash
git clone https://github.com/sivasinc/user-service.git
cd user-service
```

### 2. Start PostgreSQL
```bash
docker run -d \
  --name postgres-dev \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=userdb \
  -p 5432:5432 \
  postgres:15-alpine
```

### 3. Run the application
```bash
mvn spring-boot:run
```

### 4. Access the application

- API: http://localhost:8080/api/users
- Health: http://localhost:8080/actuator/health
- Swagger: http://localhost:8080/swagger-ui.html (if configured)

## Testing

### Run unit tests
```bash
mvn test
```

### Run integration tests
```bash
mvn verify
```

### Run all tests with coverage
```bash
mvn clean verify
```

### View coverage report
```bash
open target/site/jacoco/index.html
```

## API Endpoints

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/users/search?firstName={name}` | Search users by first name |

### Example: Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

## CI/CD Pipeline

The project uses GitHub Actions for CI/CD with the following jobs:

1. **Code Quality & Security** - OWASP, Trivy scans
2. **Build & Unit Tests** - Maven build and unit tests
3. **Integration Tests** - Testcontainers-based integration tests
4. **SonarCloud Analysis** - Code quality and coverage analysis
5. **Build Summary** - Overall pipeline status

### Pipeline Triggers

- Push to `main`, `develop`, or `feature/**` branches
- Pull requests to `main` or `develop`
- Manual workflow dispatch

## Code Coverage

Current coverage thresholds:
- **Line Coverage:** 70% minimum
- **Branch Coverage:** 60% minimum

View coverage reports:
- Locally: `target/site/jacoco/index.html`
- SonarCloud: https://sonarcloud.io/dashboard?id=sivasinc_user-service

## Project Structure
```
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/circles/userservice/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Exception handling
│   │   │   ├── model/           # JPA entities
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── security/        # Security configuration
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       └── application.yml  # Application configuration
│   └── test/
│       └── java/com/circles/userservice/
│           ├── controller/      # Controller tests
│           ├── service/         # Service tests
│           └── integration/     # Integration tests
├── .github/
│   └── workflows/
│       └── build-and-test.yml   # CI/CD pipeline
├── pom.xml                       # Maven configuration
├── sonar-project.properties      # SonarCloud configuration
└── README.md
```

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests: `mvn clean verify`
4. Commit and push
5. Create a pull request

## License

This project is licensed under the MIT License.

## Contact

For questions or issues, please open a GitHub issue.
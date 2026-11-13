# IAM Service

IAM Service is an OAuth 2.1 and OpenID Connect (OIDC 1.0) Authorization Server built with Spring Authorization Server, responsible for authentication, authorization, and token issuance across the microservices ecosystem.

## Tech Stack

- **Framework:** Spring Boot, Spring Authorization Server
- **Security:** Spring Security (Form Login, OAuth2 Client, PKCE)
- **Database:** PostgreSQL, Liquibase
- **UI Engine:** JTE (Java Template Engine)
- **Architecture:** Hexagonal Architecture (Ports and Adapters)

## Module Structure

- `iam-domain-core`: Pure domain models, value objects, and business rules
- `iam-application-service`: Use cases, input/output ports, and service interfaces
- `iam-persistence`: Database adapters, JPA repositories, entities, and Liquibase scripts
- `iam-infrastructure`: Security configurations, OAuth2 settings, and JWT customization
- `iam-web`: REST controllers, web controllers, and JTE templates
- `iam-main`: Application entry point and configuration properties

## How to Run

1. Start the PostgreSQL database
2. Run the application using the Gradle wrapper from the project root:

```bash
./gradlew :microservice:iam-service:iam-main:bootRun
```

The service runs on port `9000`.

## Key URLs

- **Login Page:** http://localhost:9000/login
- **Profile Page:** http://localhost:9000/
- **OIDC Discovery:** http://localhost:9000/.well-known/openid-configuration
- **JWKS Key Set:** http://localhost:9000/oauth2/jwks
- **Management API:** http://localhost:9000/api/v1/users


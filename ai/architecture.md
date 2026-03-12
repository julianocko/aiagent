# architecture.md
## REST API Architecture Standard
Version: 2.3

This document defines the architectural standards used by the AI development agent
for generating REST APIs using Java 25 and Spring Boot.

## Application Configuration

The application must use Spring Boot YAML configuration files located in:

src/main/resources

The agent must always generate:

- application.yaml
- application-dev.yaml
- application-staging.yaml
- application-prod.yaml

The base configuration must be defined in application.yaml.

Environment-specific files must override the base configuration using Spring profiles.

## Mandatory Base Configuration

application.yaml must include, at minimum:

- application name
- active profile
- datasource configuration
- Flyway configuration
- server port
- context path
- Keycloak resource server configuration
- Keycloak integration properties used by the application
- springdoc/OpenAPI paths
- management endpoint exposure
- base logging pattern

## Datasource Rules

Datasource configuration must be defined only for PostgreSQL and must include:

- spring.datasource.url
- spring.datasource.username
- spring.datasource.password
- spring.datasource.driver-class-name=org.postgresql.Driver

No configuration for any other database engine may be generated.

## Server Rules

The application port must be configurable in application.yaml using:

- server.port

The context path must also be configurable when needed.

## Keycloak Rules

application.yaml must include the configuration necessary for:

- OAuth2 Resource Server JWT validation
- issuer-uri
- jwk-set-uri
- realm
- client-id
- token-uri
- client authentication method private_key_jwt
- signing algorithm RS256

## Environment Files

application-dev.yaml, application-staging.yaml, and application-prod.yaml must exist.

Environment-specific logging configuration must be defined in those files.

The agent must preserve or follow the environment-specific patterns adopted by the project.

## Flyway Rules

Flyway configuration must be defined in application.yaml and must keep the schema history table in public.

Example:

spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    default-schema: public

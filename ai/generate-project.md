# generate-project.md
## Project Generation Specification
Version: 1.3

This skill defines how the AI development agent must generate a complete base project
for a Java 25 + Spring Boot REST API.

## Mandatory resource files

The generated project MUST include in src/main/resources:

- application.yaml
- application-dev.yaml
- application-staging.yaml
- application-prod.yaml

## application.yaml rules

application.yaml must contain the base configuration for:

- PostgreSQL datasource access
- Flyway
- application/server port
- context path
- Keycloak resource server
- Keycloak client integration properties
- springdoc/OpenAPI
- management endpoints
- response/request-id support base settings

## Environment YAML rules

application-dev.yaml, application-staging.yaml, and application-prod.yaml must override the base configuration using Spring profiles.

When the project provides environment templates, the generator must preserve their logging structure and patterns.

## Minimum Acceptance Criteria

A generated project is valid only if:

- all four YAML files exist
- application.yaml contains datasource, server port, and Keycloak access settings
- environment files are profile-aware
- PostgreSQL is the only configured database target

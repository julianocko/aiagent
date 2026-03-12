# generate-project.md
## Project Generation Specification
Version: 1.0

This skill defines how the AI development agent must generate a complete base project
for a Java 25 + Spring Boot REST API.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- template-variables.md
- response contract
- keycloak security guidelines

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate a production-ready project foundation, not just isolated files.

The generated project should be ready to receive features and CRUD generation.

---

# 2. Mandatory Outputs

A generated project MUST include, at minimum:

## Build and root files
- pom.xml
- README.md
- .gitignore
- optional Dockerfile
- optional docker-compose.yml

## Source structure
src/main/java/<basePackage>/
  common/
    exception/
    response/
    security/
    config/
  features/

src/test/java/<basePackage>/

## Resources
src/main/resources/
  application.yaml
  application-dev.yaml
  application-prod.yaml
  db/migration/

## Shared cross-cutting artifacts
- response wrapper
- global exception handler
- security base configuration
- OpenAPI base config
- requestId/meta support

---

# 3. Technology Rules

The generated project MUST use:
- Java 25
- Spring Boot
- PostgreSQL 17.5
- Flyway
- YAML only
- Keycloak Resource Server
- JaCoCo
- OpenAPI
- JUnit 5

---

# 4. Minimum Acceptance Criteria

A generated project is valid only if:
- it is build-ready
- shared cross-cutting structures exist
- YAML config is prepared
- Flyway is prepared
- security is prepared
- OpenAPI is prepared
- test and coverage infrastructure exists

---

# 5. Final Rule

When the user requests "generate project <name>", the agent must generate a usable project foundation,
not only a pom.xml or a folder tree.

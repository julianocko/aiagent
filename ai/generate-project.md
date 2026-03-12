# generate-project.md
## Project Generation Specification
Version: 1.2

This skill defines how the AI development agent must generate a complete base project
for a Java 25 + Spring Boot REST API.

---

# 1. Goal

Generate a production-ready project foundation, not just isolated files.

---

# 2. Technology Rules

The generated project MUST use:
- Java 25
- Spring Boot
- PostgreSQL latest only
- Flyway
- YAML only
- Keycloak Resource Server
- JaCoCo
- OpenAPI
- JUnit 5

The generator MUST NOT create:
- any configuration for another database
- any dependency for another database
- any profile for another database
- embedded fallback databases such as H2

---

# 3. PostgreSQL Rules

The generated project must include:
- PostgreSQL JDBC driver only
- Flyway configured only for PostgreSQL
- datasource YAML configured only for PostgreSQL
- Docker/Docker Compose examples only for PostgreSQL when generated

The generated project must not include multi-database compatibility.

---

# 4. Identifier Rules

The generated project must establish UUID as the default primary key strategy.

This includes:
- entities modeled with UUID ids
- migrations using `UUID PRIMARY KEY DEFAULT uuid_generate_v4()`
- PostgreSQL extension `uuid-ossp` enabled in migrations when needed

The generator must never use numeric auto-increment primary keys.

---

# 5. Minimum Acceptance Criteria

A generated project is valid only if:
- it is build-ready
- Flyway is prepared
- security is prepared
- OpenAPI is prepared
- test and coverage infrastructure exists
- PostgreSQL is the only configured database target
- UUID is the mandatory primary key strategy

---

# 6. Final Rule

When the user requests "generate project <name>", the agent must generate a usable project foundation,
not only a pom.xml or a folder tree, and it must target PostgreSQL latest exclusively with UUID primary keys by default.

# generate-repository-impl.md
## Repository Implementation Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate repository implementations
for a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- generate-entity.md
- generate-usecase.md
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate the infrastructure-side implementation of a domain repository using
Spring Data JPA and PostgreSQL.

---

# 2. Mandatory Outputs

The skill may generate:
- domain repository implementation adapter
- Spring Data JPA repository interface
- persistence entity
- persistence mapper
- custom query methods
- integration tests

---

# 3. Rules

Repository implementations MUST:
- live in infrastructure
- implement domain repository abstractions
- isolate Spring Data/JPA concerns from the application and domain layers
- support pagination and filtering when required
- use PostgreSQL only

Repository implementations MUST NOT:
- leak JPA entities to the application layer
- place business rules inside persistence code

---

# 4. Mapping Rules

When the project separates persistence and domain models, the generator must create:
- <Entity>JpaEntity
- SpringData<Entity>Repository
- <Entity>PersistenceMapper
- <Entity>RepositoryImpl

---

# 5. Query Rules

Custom queries should be generated only when justified, such as:
- existsBy...
- findByStatus
- findByNameContainingIgnoreCase
- paginated filtered queries

---

# 6. Testing Rules

The skill should generate repository integration tests when:
- custom queries exist
- mapping is non-trivial
- pagination/filtering logic is involved

---

# 7. Minimum Acceptance Criteria

A generated repository implementation is valid only if:
- it respects the domain abstraction
- it isolates infrastructure concerns
- it uses PostgreSQL/Flyway conventions
- it supports required query behavior

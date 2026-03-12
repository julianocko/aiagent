# generate-test.md
## Test Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate tests
for a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- JaCoCo thresholds
- response contract
- enums guidelines

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate meaningful automated tests that validate domain rules,
application orchestration, REST behavior, security expectations,
and persistence behavior when relevant.

---

# 2. Test Categories

The agent may generate:

## Domain tests
- entity invariants
- domain service behavior
- enum-backed exception paths

## Application tests
- use case success path
- use case failure paths
- repository interaction
- orchestration behavior

## Controller/Web tests
- success responses
- validation failures
- unauthorized/forbidden
- not found/conflict
- response envelope contract

## Persistence tests
- custom query behavior
- mapping correctness
- pagination/filtering integration

---

# 3. Coverage Rules

JaCoCo thresholds:
- line coverage >= 90%
- branch coverage >= 80%

The generator must produce tests that materially contribute to these thresholds.

---

# 4. Test Design Rules

Tests MUST be:
- intention-revealing
- isolated when possible
- named by behavior
- aligned with TDD-style expectations

Examples:
- shouldCreateClientWhenInputIsValid
- shouldReturn404WhenClientDoesNotExist
- shouldRejectActivationWhenClientIsAlreadyActive

---

# 5. Response Contract Tests

Controller tests must verify:
- data/meta structure on success
- pagination meta when applicable
- Problem Details on errors

---

# 6. Minimum Acceptance Criteria

A generated test suite is valid only if:
- it covers success and failure paths
- it verifies business rules where they exist
- it verifies response contract where applicable
- it contributes to JaCoCo thresholds

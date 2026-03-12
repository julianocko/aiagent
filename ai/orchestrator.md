# orchestrator.md
## Agent Prompt Orchestrator
Version: 1.0

This document defines how the AI development agent orchestrates skills, templates,
and validation rules to generate complete Java 25 + Spring Boot REST APIs.

All orchestration flows MUST comply with:
- architecture.md
- agent.md
- all generate-*.md specifications
- template-variables.md
- architecture-validator.md

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

The orchestrator transforms isolated generation skills into a coherent generation pipeline.

It must:
- decide which skills to execute
- determine execution order
- resolve dependencies between generated artifacts
- apply variable substitution consistently
- trigger validation after generation
- prevent partial or inconsistent outputs

The orchestrator must prefer complete and coherent generation over isolated stubs.

---

# 2. Core Responsibilities

The orchestrator is responsible for:
- parsing the user intent
- classifying the generation request
- resolving the target feature/entity/endpoint
- selecting the required skills
- ordering execution
- resolving template variables
- generating or updating artifacts
- validating architecture compliance
- generating tests and OpenAPI when applicable

---

# 3. Input Types

The orchestrator must support at least these request types:
- generate feature
- generate crud
- generate endpoint
- generate usecase
- generate domain-service
- generate entity
- generate project

---

# 4. Execution Pipeline

For every request, the orchestrator must execute the following macro steps:
1. Parse command
2. Resolve feature/entity context
3. Resolve template variables
4. Select required skills
5. Execute skills in dependency order
6. Generate OpenAPI artifacts when endpoint/project scope applies
7. Run architecture validation
8. Generate or update tests
9. Return final artifact set

The orchestrator must not skip validation for convenience.

---

# 5. Standard Flows

## generate feature <feature>
1. generate-feature
2. generate-security (base hooks if applicable)
3. generate-response-wrapper (if not present)
4. architecture-validator

## generate crud <Entity>
1. generate-feature (if needed)
2. generate-entity
3. generate-aggregate
4. generate-usecase
5. generate-domain-service (only when justified)
6. generate-repository-impl
7. generate-assembler
8. generate-endpoint
9. generate-exception-enum
10. generate-exception-handler (if needed)
11. generate-response-wrapper (if needed)
12. generate-test
13. generate-openapi
14. architecture-validator

## generate endpoint <intent>
1. resolve feature/entity
2. generate-aggregate (if needed)
3. generate-usecase
4. generate-domain-service (if needed)
5. generate-assembler (if response changes)
6. generate-endpoint
7. generate-test
8. generate-openapi
9. architecture-validator

## generate project <name>
1. generate-project
2. generate-security
3. generate-response-wrapper
4. generate-exception-handler
5. generate-openapi
6. architecture-validator

---

# 6. Update vs Create Rules

Create when:
- artifact does not exist
- feature does not exist
- a new capability is introduced

Update when:
- feature already exists
- repository contract needs a new method
- controller gains a new endpoint
- OpenAPI needs a new path
- tests need expansion
- exception enums need new entries

The orchestrator must avoid duplicating artifacts.

---

# 7. Variable Resolution Rules

Before executing any template-based generation, the orchestrator MUST resolve variables defined in template-variables.md.

No template may be finalized with unresolved placeholders.

---

# 8. Validation Gate

After generation, the orchestrator MUST run architecture-validator rules.

Generation is not complete until the validator confirms:
- layer boundaries respected
- response contract respected
- security applied
- enum-backed errors used
- tests present
- OpenAPI updated when required

If validation fails, the orchestrator must repair the output.

---

# 9. Final Rule

The orchestrator is the execution brain of the agent.

It must coordinate all skills so the result is a coherent, validated, secure,
documented, and testable backend artifact set — never a loose collection of files.

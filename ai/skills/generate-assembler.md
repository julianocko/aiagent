# generate-assembler.md
## Assembler Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate assemblers and mapping
components for REST responses in a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- generate-aggregate.md
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate the components responsible for converting:
- domain -> output aggregate
- aggregate -> response representation
- page result -> response envelope structures

The generator must clearly separate assembler responsibility from persistence mappers.

---

# 2. Rules

Assemblers MUST:
- live outside the domain layer
- keep controllers thin
- prevent manual field mapping inside controllers
- support the success response contract
- support pagination metadata assembly

Assemblers MUST NOT:
- contain business rules
- access repositories directly
- build Problem Details manually when that belongs to a global handler

---

# 3. Typical Outputs

Examples:
- ClientResponseAssembler
- ClientSummaryAssembler
- PagedClientsResponseAssembler

---

# 4. HATEOAS

If the project requires HATEOAS, the assembler should be compatible with link generation.
If not, it should still assemble clean response DTOs/envelopes.

---

# 5. Minimum Acceptance Criteria

A generated assembler is valid only if:
- it maps consistently
- it keeps controllers thin
- it does not contain business logic
- it aligns with the response contract

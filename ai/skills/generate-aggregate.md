# generate-aggregate.md
## Aggregate Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate aggregates used
between layers in a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- generate-usecase.md when applicable
- response contract
- lombok guidelines

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate application/interface-facing aggregate models that isolate domain entities
from REST payloads and support the response contract.

Aggregates may represent:
- input aggregate
- output aggregate
- summary aggregate
- paged aggregate
- command/query object

The generator must never expose domain entities directly through controllers.

---

# 2. Rules

Aggregates MUST:
- live outside the domain model
- be focused on use-case or response needs
- be immutable or controlled whenever possible
- use Lombok appropriately
- avoid framework-heavy concerns unless they are request models in interface layer

Aggregates MUST NOT:
- contain persistence annotations
- become domain entities in disguise
- carry response envelope metadata directly unless they are dedicated response meta models

---

# 3. Typical Outputs

Examples:
- CreateClientCommand
- UpdateClientCommand
- ClientOutput
- ClientSummaryOutput
- PagedClientsOutput

---

# 4. Pagination Rules

Paged aggregates must expose enough information for the response wrapper to build:
- page
- size
- totalElements
- totalPages

---

# 5. Validation Rules

Input aggregates may use Jakarta Validation when they belong to the interface layer.
Application-layer aggregates should remain transport-agnostic unless architecture.md states otherwise.

---

# 6. Minimum Acceptance Criteria

A generated aggregate is valid only if:
- it has a clear responsibility
- it does not expose domain/persistence internals
- it aligns with use case intent
- it supports the response contract when applicable

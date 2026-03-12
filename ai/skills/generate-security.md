# generate-security.md
## Security Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate security-related
artifacts for a Java 25 + Spring Boot project using Keycloak.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- keycloak security guidelines

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate security configuration compatible with:
- Keycloak
- OAuth2 Resource Server
- Bearer token authentication
- scope/authority-based authorization

---

# 2. Mandatory Concerns

The skill may generate:
- Security configuration
- JWT converter or authority mapper
- endpoint authorization rules
- OpenAPI security requirements
- test support for secured endpoints

---

# 3. Rules

Generated security must:
- validate JWT tokens
- rely on issuer/audience/client claims as required
- support SCOPE_<resource>.read and SCOPE_<resource>.write
- remain centralized and consistent

Security concerns must not leak into the domain layer.

---

# 4. Minimum Acceptance Criteria

A generated security artifact is valid only if:
- it integrates with Keycloak
- it protects endpoints consistently
- it respects layer boundaries

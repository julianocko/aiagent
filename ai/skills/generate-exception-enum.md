# generate-exception-enum.md
## Exception Enum Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate enums
for business and domain messages.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- enums guidelines

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Centralize business and domain messages in enums to avoid hardcoded strings.

---

# 2. Rules

Enums MUST:
- group related errors by context
- expose at least a message
- optionally expose a code
- be reused by exceptions and handlers

Examples:
- ClientError
- AccountError
- PaymentRequestError

---

# 3. Minimum Acceptance Criteria

A generated exception enum is valid only if:
- it avoids string duplication
- it is domain-oriented
- it is reused by exceptions

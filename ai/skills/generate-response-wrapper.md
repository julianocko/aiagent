# generate-response-wrapper.md
## Response Wrapper Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate the standard
success response wrapper used by the API.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate the reusable structures needed to produce standardized responses:

Success:
{
  "data": {},
  "meta": {
    "requestId": "uuid",
    "timestamp": "ISO-8601"
  }
}

Paginated:
{
  "data": [],
  "meta": {
    "requestId": "uuid",
    "timestamp": "ISO-8601",
    "pagination": {}
  }
}

---

# 2. Typical Outputs

The skill may generate:
- ApiResponse<T>
- ResponseMeta
- PaginationMeta
- factory/helper components
- requestId extraction helper

---

# 3. Rules

The wrapper MUST:
- be transport-focused, not domain-focused
- be reusable across endpoints
- support pagination
- not mix success and error payload styles

Errors must still use Problem Details.

---

# 4. Minimum Acceptance Criteria

A generated response wrapper is valid only if:
- it matches the response contract
- it is reusable
- it supports pagination metadata

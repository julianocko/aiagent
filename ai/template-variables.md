# template-variables.md
## Template Variable Engine Specification
Version: 1.0

This document defines the standard template variables used by the AI development agent.

All templates MUST use only approved variables, and all variables MUST be resolved
before the final artifact is produced.

---

# 1. Goal

Provide a deterministic and reusable variable system for:
- Java templates
- SQL templates
- YAML templates
- OpenAPI templates
- Markdown generation specs

This ensures naming consistency across all generated artifacts.

---

# 2. Syntax

Variables MUST use this syntax:
${variableName}

---

# 3. Core Variables

## Entity naming
- ${Entity}: PascalCase singular, example Client
- ${entity}: camelCase singular, example client
- ${Entities}: PascalCase plural, example Clients
- ${entities}: lowercase plural resource name, example clients

## Feature naming
- ${feature}: lowercase feature/package name, example client
- ${Feature}: PascalCase feature name, example Client

## Project naming
- ${basePackage}: example com.prospera.api
- ${artifactId}: example client-api
- ${projectName}: example Client API
- ${projectDescription}: example REST API for client management

## Database variables
- ${table}: client
- ${schema}: client

## Route variables
- ${resourcePath}: /clients
- ${resourceIdPath}: /clients/{id}
- ${scopeRead}: SCOPE_client.read
- ${scopeWrite}: SCOPE_client.write

## Class variables
- ${Controller}: ClientController
- ${Repository}: ClientRepository
- ${RepositoryImpl}: ClientRepositoryImpl
- ${JpaEntity}: ClientJpaEntity
- ${Mapper}: ClientMapper
- ${Assembler}: ClientAssembler
- ${ErrorEnum}: ClientError

## Use case variables
- ${CreateUseCase}: CreateClientUseCase
- ${UpdateUseCase}: UpdateClientUseCase
- ${DeleteUseCase}: DeleteClientUseCase
- ${FindByIdUseCase}: FindClientByIdUseCase
- ${ListUseCase}: ListClientsUseCase
- ${ActionUseCase}: ActivateClientUseCase

## Request/response variables
- ${CreateRequest}: CreateClientRequest
- ${UpdateRequest}: UpdateClientRequest
- ${PatchRequest}: PatchClientRequest
- ${Response}: ClientResponse
- ${SummaryResponse}: ClientSummaryResponse
- ${Aggregate}: ClientAggregate

## Migration variables
- ${migrationVersion}: 1.0.0
- ${migrationAction}: create_client_table
- ${migrationFile}: V1.0.0__create_client_table.sql

---

# 4. Resolution Rules

The engine MUST:
- pluralize consistently
- support case conversion
- validate unresolved placeholders
- keep names aligned across Java, SQL, YAML, and OpenAPI files

Example for Client:
- ${Entity} -> Client
- ${entity} -> client
- ${Entities} -> Clients
- ${entities} -> clients
- ${feature} -> client
- ${scopeRead} -> SCOPE_client.read
- ${scopeWrite} -> SCOPE_client.write

---

# 5. Final Rule

No generated artifact is valid if it contains unresolved placeholders
or inconsistent derived names.

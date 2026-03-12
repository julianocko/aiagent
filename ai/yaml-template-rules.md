# YAML Template Rules for the Agent

These files are model templates for the agent and should be stored under:

templates/
  application.yaml.template
  application-dev.yaml.template
  application-staging.yaml.template
  application-prod.yaml.template

## Important Rule

Spring Boot already uses `${...}` for environment placeholders.
Because of that, the agent template variables for YAML files must use:

`{{variableName}}`

This avoids conflicts between:
- agent template variables
- Spring runtime placeholders

## Supported template variables

- `{{artifactId}}`
- `{{basePackage}}`
- `{{databaseName}}`
- `{{serverPort}}`
- `{{contextPath}}`
- `{{keycloakIssuerUri}}`
- `{{keycloakJwkSetUri}}`
- `{{keycloakRealm}}`
- `{{keycloakClientId}}`
- `{{keycloakTokenUri}}`

## Example

Agent model:
`name: {{artifactId}}`

Rendered output:
`name: client-api`

Spring runtime placeholder example:
`server.port: ${SERVER_PORT:8080}`

This placeholder must remain unchanged in the rendered file.

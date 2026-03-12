-- V1.0.0__create_${entity}_table.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA public;

CREATE SCHEMA IF NOT EXISTS ${feature};

CREATE TABLE IF NOT EXISTS ${feature}.${entity} (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    callback_url VARCHAR(512),
    description VARCHAR(1024),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_${entity}_name
    ON ${feature}.${entity} (name);

CREATE INDEX IF NOT EXISTS idx_${entity}_active
    ON ${feature}.${entity} (active);

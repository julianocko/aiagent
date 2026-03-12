-- V1.0.0__create_client_table.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA public;

CREATE SCHEMA IF NOT EXISTS client;

CREATE TABLE IF NOT EXISTS client.client (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    callback_url VARCHAR(512),
    description VARCHAR(1024),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_client_name
    ON client.client (name);

CREATE INDEX IF NOT EXISTS idx_client_active
    ON client.client (active);

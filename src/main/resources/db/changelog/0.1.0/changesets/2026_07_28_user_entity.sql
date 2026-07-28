--liquibase formatted sql

--changeset pulsar:create_users_table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(128) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) NULL,
    display_name VARCHAR(128) NOT NULL,
    birthday DATE NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

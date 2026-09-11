--liquibase formatted sql

--changeset pulsar:create_chats_table
CREATE TABLE chats
(
    id         UUID PRIMARY KEY,
    title      VARCHAR(32),
    type       VARCHAR(32) NOT NULL,
    avatar_url VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_chats_title ON chats (title);

--rollback DROP TABLE chats;

--changeset pulsar:create_table_chat_members
CREATE TABLE chat_members
(
    chat_id   UUID REFERENCES chats (id) ON DELETE CASCADE,
    user_id   UUID REFERENCES users (id) ON DELETE RESTRICT,
    role      VARCHAR(32) NOT NULL,
    joined_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_chat_members PRIMARY KEY (chat_id, user_id)
);

--rollback DROP TABLE chat_members;

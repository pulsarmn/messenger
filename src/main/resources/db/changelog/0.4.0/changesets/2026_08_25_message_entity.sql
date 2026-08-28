--liquibase formatted sql

--changeset pulsarmn:create_table_messages
CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    chat_id    UUID        NOT NULL REFERENCES chats (id),
    sender_id  UUID        NOT NULL REFERENCES users (id),
    type       VARCHAR(32) NOT NULL,
    status     VARCHAR(32) NOT NULL,
    text       TEXT,
    file_key   VARCHAR(512),
    file_name  VARCHAR(256),
    file_size  BIGINT,
    mime_type  VARCHAR(128),
    width      INT,
    height     INT,
    duration   INT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_messages_chat_id_id ON messages (chat_id, id DESC);

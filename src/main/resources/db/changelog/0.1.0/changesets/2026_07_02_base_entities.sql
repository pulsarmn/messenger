--liquibase formatted sql

--changeset pulsarmn:create-users-table
CREATE TABLE IF NOT EXISTS users
(
    id            UUID PRIMARY KEY,
    username      VARCHAR(128) UNIQUE      NOT NULL,
    password_hash VARCHAR(255)             NOT NULL,
    display_name  VARCHAR(128)             NOT NULL DEFAULT '',
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

--changeset pulsarmn:create-refresh-tokens-table
CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id         UUID PRIMARY KEY,
    token_hash VARCHAR(128)               NOT NULL,
    user_id    UUID REFERENCES users (id) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE   NOT NULL DEFAULT NOW()
);

--changeset pulsarmn:create-chats-table
CREATE TABLE IF NOT EXISTS chats
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

--changeset pulsarmn:create-chat-members-table
CREATE TABLE IF NOT EXISTS chat_members
(
    chat_id UUID REFERENCES chats (id) ON DELETE CASCADE NOT NULL,
    user_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT chat_members_pkey PRIMARY KEY (chat_id, user_id)
);

--changeset pulsarmn:create-messages-table
CREATE TABLE IF NOT EXISTS messages
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    chat_id    UUID REFERENCES chats (id) NOT NULL,
    sender_id  UUID REFERENCES users (id) NOT NULL,
    content    VARCHAR(4096)              NOT NULL,
    is_read    BOOLEAN                    NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE   NOT NULL DEFAULT NOW()
);

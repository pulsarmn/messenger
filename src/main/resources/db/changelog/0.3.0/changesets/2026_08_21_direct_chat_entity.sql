--liquibase formatted sql

--changeset pulsarmn:create_direct_chats_table
CREATE TABLE direct_chats
(
    lower_user_id  UUID        NOT NULL REFERENCES users (id),
    higher_user_id UUID        NOT NULL REFERENCES users (id),
    chat_id        UUID        NOT NULL REFERENCES chats (id) ON DELETE CASCADE,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_direct_chats PRIMARY KEY (lower_user_id, higher_user_id),
    CONSTRAINT chk_user_order CHECK ( lower_user_id < higher_user_id )
);

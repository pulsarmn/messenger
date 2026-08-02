--liquibase formatted sql

--changeset pulsarmn:create_refresh_tokens_table
CREATE TABLE refresh_tokens
(
    id         UUID PRIMARY KEY,
    token_hash VARCHAR(255)                                 NOT NULL,
    user_id    UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    expires_at TIMESTAMPTZ                                  NOT NULL,
    created_at TIMESTAMPTZ                                  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_token_hash ON refresh_tokens(token_hash);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

--rollback DROP TABLE refresh_tokens;

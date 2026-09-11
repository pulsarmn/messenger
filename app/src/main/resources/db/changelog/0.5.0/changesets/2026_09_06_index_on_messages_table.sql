--liquibase formatted sql

--changeset pulsarmn:create_an_index_on_chat_id_and_created_at_fields_of_messages_table
CREATE INDEX idx_messages_chat_id_created_at ON messages (chat_id, created_at DESC);

--rollback DROP INDEX idx_messages_chat_id_created_at;

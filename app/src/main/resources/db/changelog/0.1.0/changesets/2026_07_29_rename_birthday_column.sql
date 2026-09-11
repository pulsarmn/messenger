--liquibase formatted sql

--changeset pulsarmn:rename_birthday_column
ALTER TABLE users RENAME birthday TO birthdate;

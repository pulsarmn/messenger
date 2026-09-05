package ru.pulsarmn.messenger.dto.response;

import ru.pulsarmn.messenger.entity.MessageStatus;
import ru.pulsarmn.messenger.entity.MessageType;

import java.time.Instant;
import java.util.UUID;


public record MessageResponse(UUID chatId,
                              UUID senderId,
                              UUID messageId,
                              String text,
                              MessageStatus status,
                              MessageType type,
                              Instant createdAt) {

    public static class Builder {
        private UUID chatId;
        private UUID senderId;
        private UUID messageId;
        private String text;
        private MessageStatus status;
        private MessageType type;
        private Instant createdAt;

        public Builder chatId(UUID chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder senderId(UUID senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder messageId(UUID messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder status(MessageStatus status) {
            this.status = status;
            return this;
        }

        public Builder type(MessageType type) {
            this.type = type;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public MessageResponse build() {
            return new MessageResponse(chatId, senderId, messageId, text, status, type, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

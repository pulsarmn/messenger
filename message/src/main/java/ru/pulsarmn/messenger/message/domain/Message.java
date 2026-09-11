package ru.pulsarmn.messenger.message.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(schema = "public", name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @JoinColumn(name = "chat_id")
    private UUID chatId;

    @JoinColumn(name = "sender_id")
    private UUID senderId;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private MessageType type;

    @Column(name = "text")
    private String text;

    @Embedded
    private Attachment attachment;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private MessageStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public enum Status {
        SENT,
        DELIVERED,
        READ
    }

    public Message() {
    }

    public Message(UUID id, UUID chatId, UUID senderId, MessageType type, String text, Attachment attachment, MessageStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.type = type;
        this.text = text;
        this.attachment = attachment;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Builder {
        private UUID id;
        private UUID chatId;
        private UUID senderId;
        private MessageType type;
        private String text;
        private Attachment attachment;
        private MessageStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder chatId(UUID chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder senderId(UUID senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder type(MessageType type) {
            this.type = type;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder attachment(Attachment attachment) {
            this.attachment = attachment;
            return this;
        }

        public Builder status(MessageStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Message build() {
            return new Message(id, chatId, senderId, type, text, attachment, status, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Message message = (Message) object;
        return Objects.equals(id, message.id) && Objects.equals(chatId, message.chatId) && Objects.equals(senderId, message.senderId) && type == message.type && Objects.equals(text, message.text) && Objects.equals(attachment, message.attachment) && status == message.status && Objects.equals(createdAt, message.createdAt) && Objects.equals(updatedAt, message.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, senderId, type, text, attachment, status, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", senderId=" + senderId +
                ", type=" + type +
                ", text='" + text + '\'' +
                ", attachment=" + attachment +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

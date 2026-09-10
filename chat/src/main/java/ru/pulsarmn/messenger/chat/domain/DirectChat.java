package ru.pulsarmn.messenger.chat.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(schema = "public", name = "direct_chats")
public class DirectChat {

    @EmbeddedId
    private DirectChatId id;

    @JoinColumn(name = "chat_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Chat chat;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public DirectChat() {
    }

    public DirectChat(UUID lowerUserId, UUID higherUserId, Chat chat, Instant createdAt) {
        this.id = new DirectChatId(lowerUserId, higherUserId);
        this.chat = chat;
        this.createdAt = createdAt;
    }

    public DirectChatId getId() {
        return id;
    }

    public void setId(DirectChatId id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public UUID getChatId() {
        return chat != null ? chat.getId() : null;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private UUID lowerUserId;
        private UUID higherUserId;
        private Chat chat;
        private Instant createdAt;

        public Builder lowerUserId(UUID lowerUserId) {
            this.lowerUserId = lowerUserId;
            return this;
        }

        public Builder higherUserId(UUID higherUserId) {
            this.higherUserId = higherUserId;
            return this;
        }

        public Builder chat(Chat chat) {
            this.chat = chat;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DirectChat build() {
            return new DirectChat(lowerUserId, higherUserId, chat, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        DirectChat that = (DirectChat) object;
        return Objects.equals(id, that.id) && Objects.equals(chat, that.chat) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat, createdAt);
    }

    @Override
    public String toString() {
        return "DirectChat{" +
                "id=" + id +
                ", chat=" + chat +
                ", createdAt=" + createdAt +
                '}';
    }
}

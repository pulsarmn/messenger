package ru.pulsarmn.messenger.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(schema = "public", name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    public enum Type {
        DIRECT,
        GROUP
    }

    public Chat() {}

    public Chat(UUID id, String title, Type type, String avatarUrl, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private UUID id;
        private String title;
        private Type type = Type.DIRECT;
        private String avatarUrl;
        private Instant createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Chat build() {
            return new Chat(id, title, type, avatarUrl, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Chat chat = (Chat) object;
        return Objects.equals(id, chat.id) && Objects.equals(title, chat.title) && type == chat.type && Objects.equals(avatarUrl, chat.avatarUrl) && Objects.equals(createdAt, chat.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type, avatarUrl, createdAt);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

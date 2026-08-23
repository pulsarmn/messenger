package ru.pulsarmn.messenger.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;


@Entity
@Table(schema = "public", name = "direct_chats")
public class DirectChat {

    @EmbeddedId
    private DirectChatId id;

    @MapsId("lowerUserId")
    @JoinColumn(name = "lower_user_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User lowerUser;

    @MapsId("higherUserId")
    @JoinColumn(name = "higher_user_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User higherUser;

    @JoinColumn(name = "chat_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Chat chat;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public DirectChat() {}

    public DirectChat(DirectChatId id, User lowerUser, User higherUser, Chat chat, Instant createdAt) {
        this.id = id;
        this.lowerUser = lowerUser;
        this.higherUser = higherUser;
        this.chat = chat;
        this.createdAt = createdAt;
    }

    public DirectChatId getId() {
        return id;
    }

    public void setId(DirectChatId id) {
        this.id = id;
    }

    public User getLowerUser() {
        return lowerUser;
    }

    public void setLowerUser(User lowerUser) {
        this.lowerUser = lowerUser;
    }

    public User getHigherUser() {
        return higherUser;
    }

    public void setHigherUser(User higherUser) {
        this.higherUser = higherUser;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public static class Builder {
        private User lowerUser;
        private User higherUser;
        private Chat chat;
        private Instant createdAt;

        public Builder lowerUser(User lowerUser) {
            this.lowerUser = lowerUser;
            return this;
        }

        public Builder higherUser(User higherUser) {
            this.higherUser = higherUser;
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
            return new DirectChat(null, lowerUser, higherUser, chat, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        DirectChat that = (DirectChat) object;
        return Objects.equals(id, that.id) && Objects.equals(lowerUser, that.lowerUser) && Objects.equals(higherUser, that.higherUser) && Objects.equals(chat, that.chat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lowerUser, higherUser, chat);
    }

    @Override
    public String toString() {
        return "DirectChat{" +
                "id=" + id +
                ", lowerUser=" + lowerUser +
                ", higherUser=" + higherUser +
                ", chat=" + chat +
                '}';
    }
}

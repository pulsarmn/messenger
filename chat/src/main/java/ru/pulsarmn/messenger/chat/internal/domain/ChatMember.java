package ru.pulsarmn.messenger.chat.internal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(schema = "public", name = "chat_members")
public class ChatMember {

    @EmbeddedId
    private ChatMemberId id;

    @MapsId("chatId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Chat chat;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ChatRole role;

    @CreationTimestamp
    @Column(name = "joined_at")
    private Instant joinedAt;

    public enum ChatRole {
        MEMBER,
        ADMIN,
        OWNER
    }

    public ChatMember() {}

    public ChatMember(Chat chat, UUID userId, ChatRole role, Instant joinedAt) {
        this.id = new ChatMemberId(chat.getId(), userId);
        this.chat = chat;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public ChatMemberId getId() {
        return id;
    }

    public void setId(ChatMemberId id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public UUID getChatId() {
        return id != null ? id.getChatId() : chat.getId();
    }

    public UUID getUserId() {
        return id == null ? null : id.getUserId();
    }

    public ChatRole getRole() {
        return role;
    }

    public void setRole(ChatRole role) {
        this.role = role;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public static class Builder {
        private Chat chat;
        private UUID userId;
        private ChatRole role = ChatRole.MEMBER;
        private Instant joinedAt;

        public Builder chat(Chat chat) {
            this.chat = chat;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder role(ChatRole role) {
            this.role = role;
            return this;
        }

        public Builder joinedAt(Instant joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        public ChatMember build() {
            return new ChatMember(chat, userId, role, joinedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ChatMember that = (ChatMember) object;
        return Objects.equals(id, that.id) && role == that.role && Objects.equals(joinedAt, that.joinedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, joinedAt);
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "id=" + id +
                ", role=" + role +
                ", joinedAt=" + joinedAt +
                '}';
    }
}

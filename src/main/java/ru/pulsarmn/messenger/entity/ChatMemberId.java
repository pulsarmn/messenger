package ru.pulsarmn.messenger.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;


@Embeddable
public class ChatMemberId {

    private UUID chatId;
    private UUID userId;

    public ChatMemberId() {}

    public ChatMemberId(UUID chatId, UUID userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ChatMemberId that = (ChatMemberId) object;
        return Objects.equals(chatId, that.chatId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId);
    }

    @Override
    public String toString() {
        return "ChatMemberId{" +
                "chatId=" + chatId +
                ", userId=" + userId +
                '}';
    }
}

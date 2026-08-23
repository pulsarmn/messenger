package ru.pulsarmn.messenger.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;


@Embeddable
public class DirectChatId {

    private UUID lowerUserId;
    private UUID higherUserId;

    public DirectChatId() {
    }

    public DirectChatId(UUID lowerUserId, UUID higherUserId) {
        this.lowerUserId = lowerUserId;
        this.higherUserId = higherUserId;
    }

    public static DirectChatId of(UUID uuid1, UUID uuid2) {
        if (uuid1.compareTo(uuid2) > 0) {
            return new DirectChatId(uuid2, uuid1);
        }
        return new DirectChatId(uuid1, uuid2);
    }

    public UUID getLowerUserId() {
        return lowerUserId;
    }

    public void setLowerUserId(UUID lowerUserId) {
        this.lowerUserId = lowerUserId;
    }

    public UUID getHigherUserId() {
        return higherUserId;
    }

    public void setHigherUserId(UUID higherUserId) {
        this.higherUserId = higherUserId;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        DirectChatId that = (DirectChatId) object;
        return Objects.equals(lowerUserId, that.lowerUserId) && Objects.equals(higherUserId, that.higherUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerUserId, higherUserId);
    }

    @Override
    public String toString() {
        return "DirectChatId{" +
                "lowerUserId=" + lowerUserId +
                ", higherUserId=" + higherUserId +
                '}';
    }
}

package ru.pulsarmn.messenger.message;

public record MessageEvent(EventType type, MessageResponse message) {

    public enum EventType {
        MESSAGE_CREATED,
        MESSAGE_UPDATED,
        MESSAGE_DELETED
    }
}

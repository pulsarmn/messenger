package ru.pulsarmn.messenger.dto;

import ru.pulsarmn.messenger.dto.response.MessageResponse;

public record MessageEvent(EventType type, MessageResponse message) {

    public enum EventType {
        MESSAGE_CREATED,
        MESSAGE_UPDATED,
        MESSAGE_DELETED
    }
}

package ru.pulsarmn.messenger.dto.request;

import ru.pulsarmn.messenger.entity.MessageType;


public record MessageCreationRequest(MessageType messageType, String text) {
}

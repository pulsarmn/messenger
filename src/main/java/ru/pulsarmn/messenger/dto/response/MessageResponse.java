package ru.pulsarmn.messenger.dto.response;

import ru.pulsarmn.messenger.entity.MessageStatus;

import java.time.Instant;
import java.util.UUID;


public record MessageResponse(UUID id,
                              MessageStatus status,
                              Instant createdAt) {
}

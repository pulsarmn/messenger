package ru.pulsarmn.messenger.chat.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record ChatCreationRequest(@NotNull UUID recipientId) {
}

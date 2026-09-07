package ru.pulsarmn.messenger.chat;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record ChatCreationRequest(@NotNull UUID recipientId) {
}

package ru.pulsarmn.messenger.chat.api.dto;

import java.util.UUID;


public record ChatMemberDto(UUID chatId, UUID userId) {
}

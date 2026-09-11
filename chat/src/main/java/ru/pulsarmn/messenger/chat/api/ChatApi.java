package ru.pulsarmn.messenger.chat.api;

import ru.pulsarmn.messenger.chat.api.dto.ChatMemberDto;

import java.util.List;
import java.util.UUID;


public interface ChatApi {

    boolean isUserInChat(UUID userId, UUID chatId);

    List<ChatMemberDto> getChatParticipants(UUID chatId);
}

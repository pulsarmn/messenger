package ru.pulsarmn.messenger.chat.internal.mapper;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.chat.internal.domain.DirectChat;
import ru.pulsarmn.messenger.chat.internal.dto.response.ChatResponse;


@Component
public class DirectChatMapper {

    public ChatResponse mapToResponse(DirectChat directChat) {
        return new ChatResponse(directChat.getChatId());
    }
}

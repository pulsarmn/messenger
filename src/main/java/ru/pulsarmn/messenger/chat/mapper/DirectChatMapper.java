package ru.pulsarmn.messenger.chat.mapper;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.chat.dto.response.ChatResponse;
import ru.pulsarmn.messenger.chat.domain.DirectChat;


@Component
public class DirectChatMapper {

    public ChatResponse mapToResponse(DirectChat directChat) {
        return new ChatResponse(directChat.getChatId());
    }
}

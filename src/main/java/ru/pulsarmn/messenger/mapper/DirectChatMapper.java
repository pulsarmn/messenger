package ru.pulsarmn.messenger.mapper;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.dto.response.ChatResponse;
import ru.pulsarmn.messenger.entity.DirectChat;


@Component
public class DirectChatMapper {

    public ChatResponse mapToResponse(DirectChat directChat) {
        return new ChatResponse(directChat.getChat().getId());
    }
}

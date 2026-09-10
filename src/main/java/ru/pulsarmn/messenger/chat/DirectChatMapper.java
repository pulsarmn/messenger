package ru.pulsarmn.messenger.chat;

import org.springframework.stereotype.Component;


@Component
public class DirectChatMapper {

    public ChatResponse mapToResponse(DirectChat directChat) {
        return new ChatResponse(directChat.getChatId());
    }
}

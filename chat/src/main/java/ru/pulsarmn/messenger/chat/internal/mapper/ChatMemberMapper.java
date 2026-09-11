package ru.pulsarmn.messenger.chat.internal.mapper;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.chat.api.dto.ChatMemberDto;
import ru.pulsarmn.messenger.chat.internal.domain.ChatMember;


@Component
public class ChatMemberMapper {

    public ChatMemberDto mapToDto(ChatMember chatMember) {
        return new ChatMemberDto(chatMember.getChatId(), chatMember.getUserId());
    }
}

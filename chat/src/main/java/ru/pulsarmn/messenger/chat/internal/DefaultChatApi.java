package ru.pulsarmn.messenger.chat.internal;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.chat.api.ChatApi;
import ru.pulsarmn.messenger.chat.api.dto.ChatMemberDto;
import ru.pulsarmn.messenger.chat.internal.domain.ChatMemberId;
import ru.pulsarmn.messenger.chat.internal.mapper.ChatMemberMapper;
import ru.pulsarmn.messenger.chat.internal.repository.ChatMemberRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class DefaultChatApi implements ChatApi {

    private final ChatMemberMapper chatMemberMapper;
    private final ChatMemberRepository chatMemberRepository;

    public DefaultChatApi(ChatMemberMapper chatMemberMapper, ChatMemberRepository chatMemberRepository) {
        this.chatMemberMapper = chatMemberMapper;
        this.chatMemberRepository = chatMemberRepository;
    }

    @Override
    public boolean isUserInChat(UUID userId, UUID chatId) {
        ChatMemberId id = new ChatMemberId(chatId, userId);
        return chatMemberRepository.findById(id)
                .isPresent();
    }

    @Override
    public List<ChatMemberDto> getChatParticipants(UUID chatId) {
        return chatMemberRepository.findAllByChatId(chatId)
                .stream()
                .map(chatMemberMapper::mapToDto)
                .collect(Collectors.toList());
    }
}

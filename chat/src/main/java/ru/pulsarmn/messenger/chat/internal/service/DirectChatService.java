package ru.pulsarmn.messenger.chat.internal.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.chat.internal.domain.Chat;
import ru.pulsarmn.messenger.chat.internal.domain.ChatMember;
import ru.pulsarmn.messenger.chat.internal.domain.DirectChat;
import ru.pulsarmn.messenger.chat.internal.domain.DirectChatId;
import ru.pulsarmn.messenger.chat.internal.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.chat.internal.dto.response.ChatResponse;
import ru.pulsarmn.messenger.chat.internal.mapper.DirectChatMapper;
import ru.pulsarmn.messenger.chat.internal.repository.ChatMemberRepository;
import ru.pulsarmn.messenger.chat.internal.repository.ChatRepository;
import ru.pulsarmn.messenger.chat.internal.repository.DirectChatRepository;
import ru.pulsarmn.messenger.user.api.UserApi;
import ru.pulsarmn.messenger.user.api.dto.response.UserDto;
import ru.pulsarmn.messenger.user.api.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;


@Service
public class DirectChatService {

    private final UserApi userApi;
    private final ChatRepository chatRepository;
    private final DirectChatMapper directChatMapper;
    private final ChatMemberRepository chatMemberRepository;
    private final DirectChatRepository directChatRepository;

    public DirectChatService(UserApi userApi, ChatRepository chatRepository, DirectChatMapper directChatMapper, ChatMemberRepository chatMemberRepository, DirectChatRepository directChatRepository) {
        this.userApi = userApi;
        this.chatRepository = chatRepository;
        this.directChatMapper = directChatMapper;
        this.chatMemberRepository = chatMemberRepository;
        this.directChatRepository = directChatRepository;
    }

    @Transactional
    public ChatResponse getOrCreateDirectChat(UUID userId, ChatCreationRequest request) {
        if (userId.equals(request.recipientId())) {
            throw new IllegalArgumentException("Cannot create a direct chat with yourself");
        }

        DirectChatId directChatId = DirectChatId.of(userId, request.recipientId());

        return directChatRepository.findById(directChatId)
                .map(directChatMapper::mapToResponse)
                .orElseGet(() -> createDirectChat(directChatId));
    }

    private ChatResponse createDirectChat(DirectChatId directChatId) {
        try {
            UserDto lowerUser = userApi.findUserById(directChatId.getLowerUserId())
                    .orElseThrow(() -> new UserNotFoundException("User with id '%s' was not found".formatted(directChatId.getLowerUserId())));
            UserDto higherUser = userApi.findUserById(directChatId.getHigherUserId())
                    .orElseThrow(() -> new UserNotFoundException("User with id '%s' was not found".formatted(directChatId.getHigherUserId())));

            Chat chat = chatRepository.save(Chat.builder().type(Chat.Type.DIRECT).build());

            List<ChatMember> chatMembers = List.of(
                    ChatMember.builder().userId(lowerUser.id()).chat(chat).role(ChatMember.ChatRole.MEMBER).build(),
                    ChatMember.builder().userId(higherUser.id()).chat(chat).role(ChatMember.ChatRole.MEMBER).build()
            );
            chatMemberRepository.saveAll(chatMembers);

            DirectChat directChat = directChatRepository.save(
                    DirectChat.builder().lowerUserId(lowerUser.id()).higherUserId(higherUser.id()).chat(chat).build());

            return directChatMapper.mapToResponse(directChat);
        } catch (DataIntegrityViolationException e) {
            return directChatRepository.findById(directChatId)
                    .map(directChatMapper::mapToResponse)
                    .orElseThrow(() -> new IllegalStateException("Failed to retrieve chat"));
        }
    }
}

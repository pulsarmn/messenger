package ru.pulsarmn.messenger.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.dto.response.ChatResponse;
import ru.pulsarmn.messenger.entity.*;
import ru.pulsarmn.messenger.mapper.DirectChatMapper;
import ru.pulsarmn.messenger.repository.ChatMemberRepository;
import ru.pulsarmn.messenger.repository.ChatRepository;
import ru.pulsarmn.messenger.repository.DirectChatRepository;

import java.util.List;
import java.util.UUID;


@Service
public class DirectChatService {

    private final UserService userService;
    private final ChatRepository chatRepository;
    private final DirectChatMapper directChatMapper;
    private final ChatMemberRepository chatMemberRepository;
    private final DirectChatRepository directChatRepository;

    public DirectChatService(UserService userService, ChatRepository chatRepository, DirectChatMapper directChatMapper, ChatMemberRepository chatMemberRepository, DirectChatRepository directChatRepository) {
        this.userService = userService;
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
            User lowerUser = userService.getUserById(directChatId.getLowerUserId());
            User higherUser = userService.getUserById(directChatId.getHigherUserId());

            Chat chat = chatRepository.save(Chat.builder().type(Chat.Type.DIRECT).build());

            List<ChatMember> chatMembers = List.of(
                    ChatMember.builder().user(lowerUser).chat(chat).role(ChatMember.ChatRole.MEMBER).build(),
                    ChatMember.builder().user(higherUser).chat(chat).role(ChatMember.ChatRole.MEMBER).build()
            );
            chatMemberRepository.saveAll(chatMembers);

            DirectChat directChat = directChatRepository.save(
                    DirectChat.builder().lowerUser(lowerUser).higherUser(higherUser).chat(chat).build());

            return directChatMapper.mapToResponse(directChat);
        } catch (DataIntegrityViolationException e) {
            return directChatRepository.findById(directChatId)
                    .map(directChatMapper::mapToResponse)
                    .orElseThrow(() -> new IllegalStateException("Failed to retrieve chat"));
        }
    }
}

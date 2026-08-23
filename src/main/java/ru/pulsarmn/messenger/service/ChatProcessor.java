package ru.pulsarmn.messenger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.dto.response.ChatResponse;
import ru.pulsarmn.messenger.entity.*;
import ru.pulsarmn.messenger.mapper.DirectChatMapper;

import java.util.UUID;


@Service
public class ChatProcessor {

    private final UserService userService;
    private final ChatService chatService;
    private final DirectChatMapper directChatMapper;
    private final DirectChatService directChatService;
    private final ChatMemberService chatMemberService;

    public ChatProcessor(UserService userService, ChatService chatService, DirectChatMapper directChatMapper, DirectChatService directChatService, ChatMemberService chatMemberService) {
        this.userService = userService;
        this.chatService = chatService;
        this.directChatMapper = directChatMapper;
        this.directChatService = directChatService;
        this.chatMemberService = chatMemberService;
    }

    @Transactional
    public ChatResponse createChat(UUID userId, ChatCreationRequest request) {
        DirectChatId directChatId = DirectChatId.of(userId, request.recipientId());

        return directChatService.findById(directChatId)
                .map(directChatMapper::mapToResponse)
                .orElseGet(() -> createAndSaveChat(directChatId));
    }

    private ChatResponse createAndSaveChat(DirectChatId directChatId) {
        User lowerUser = userService.getUserById(directChatId.getLowerUserId());
        User higherUser = userService.getUserById(directChatId.getHigherUserId());
        Chat chat = createChat();
        DirectChat directChat = createDirectChat(lowerUser, higherUser, chat);
        createChatMembers(lowerUser, higherUser, chat);
        return directChatMapper.mapToResponse(directChat);
    }

    private Chat createChat() {
        Chat chat = buildChat();
        return chatService.save(chat);
    }

    private Chat buildChat() {
        return Chat.builder()
                .type(Chat.Type.DIRECT)
                .build();
    }

    private DirectChat createDirectChat(User lowerUser, User higherUser, Chat chat) {
        DirectChat directChat = buildDirectChat(lowerUser, higherUser, chat);
        return directChatService.save(directChat);
    }

    private DirectChat buildDirectChat(User lowerUser, User higherUser, Chat chat) {
        return DirectChat.builder()
                .lowerUser(lowerUser)
                .higherUser(higherUser)
                .chat(chat)
                .build();
    }

    private void createChatMembers(User lowerUser, User higherUser, Chat chat) {
        createChatMember(lowerUser, chat);
        createChatMember(higherUser, chat);
    }

    private void createChatMember(User user, Chat chat) {
        ChatMember chatMember = buildChatMember(user, chat);
        chatMemberService.save(chatMember);
    }

    private ChatMember buildChatMember(User user, Chat chat) {
        return ChatMember.builder()
                .user(user)
                .chat(chat)
                .role(ChatMember.ChatRole.MEMBER)
                .build();
    }
}

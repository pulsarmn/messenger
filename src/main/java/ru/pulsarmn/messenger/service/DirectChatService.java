package ru.pulsarmn.messenger.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.entity.*;
import ru.pulsarmn.messenger.exception.UserNotFoundException;
import ru.pulsarmn.messenger.repository.ChatMemberRepository;
import ru.pulsarmn.messenger.repository.ChatRepository;
import ru.pulsarmn.messenger.repository.DirectChatRepository;
import ru.pulsarmn.messenger.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;


@Service
public class DirectChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final DirectChatRepository directChatRepository;

    public DirectChatService(ChatRepository chatRepository, UserRepository userRepository, ChatMemberRepository chatMemberRepository, DirectChatRepository directChatRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.directChatRepository = directChatRepository;
    }

    @Transactional
    public UUID createChat(UUID userId, ChatCreationRequest request) {
        DirectChatId directChatId = getComparedDirectChatId(userId, request.recipientId());
        Optional<DirectChat> optionalDirectChat = directChatRepository.findById(directChatId);
        if (optionalDirectChat.isPresent()) {
            return optionalDirectChat.get().getChat().getId();
        }

        User lowerUser = userRepository.findById(directChatId.getLowerUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id '%s' not found".formatted(directChatId.getLowerUserId())));
        User higherUser = userRepository.findById(directChatId.getHigherUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id '%s' not found".formatted(directChatId.getHigherUserId())));
        Chat chat = Chat.builder()
                .type(Chat.Type.DIRECT)
                .build();
        ChatMember lowerChatMember = ChatMember.builder()
                .user(lowerUser)
                .chat(chat)
                .role(ChatMember.ChatRole.MEMBER)
                .build();
        ChatMember higherChatMember = ChatMember.builder()
                .user(higherUser)
                .chat(chat)
                .role(ChatMember.ChatRole.MEMBER)
                .build();
        DirectChat directChat = DirectChat.builder()
                .lowerUser(lowerUser)
                .higherUser(higherUser)
                .chat(chat)
                .build();

        try {
            chatRepository.save(chat);
            chatMemberRepository.save(lowerChatMember);
            chatMemberRepository.save(higherChatMember);
            directChatRepository.save(directChat);
        } catch (DataIntegrityViolationException e) {
            return directChatRepository.findById(directChatId).get().getChat().getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return chat.getId();
    }

    private DirectChatId getComparedDirectChatId(UUID userId, UUID recipientId) {
        if (userId.compareTo(recipientId) > 0) {
            return new DirectChatId(recipientId, userId);
        }
        return new DirectChatId(userId, recipientId);
    }
}

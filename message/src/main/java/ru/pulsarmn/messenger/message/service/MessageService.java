package ru.pulsarmn.messenger.message.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.chat.api.ChatApi;
import ru.pulsarmn.messenger.chat.exception.ChatMemberNotFoundException;
import ru.pulsarmn.messenger.infrastructure.CursorPageResponse;
import ru.pulsarmn.messenger.message.domain.Message;
import ru.pulsarmn.messenger.message.domain.MessageStatus;
import ru.pulsarmn.messenger.message.domain.MessageType;
import ru.pulsarmn.messenger.message.dto.MessageCreationResult;
import ru.pulsarmn.messenger.message.dto.request.MessageCreationRequest;
import ru.pulsarmn.messenger.message.dto.request.MessageUpdateRequest;
import ru.pulsarmn.messenger.message.dto.response.MessageResponse;
import ru.pulsarmn.messenger.message.exception.MessageNotFoundException;
import ru.pulsarmn.messenger.message.exception.MessageOwnershipException;
import ru.pulsarmn.messenger.message.repository.MessageRepository;
import ru.pulsarmn.messenger.user.api.UserApi;
import ru.pulsarmn.messenger.user.api.dto.response.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class MessageService {

    private final UserApi userApi;
    private final ChatApi chatApi;
    private final MessageRepository messageRepository;

    private static final int MESSAGES_LIMIT = 50;

    public MessageService(UserApi userApi, ChatApi chatApi, MessageRepository messageRepository) {
        this.userApi = userApi;
        this.chatApi = chatApi;
        this.messageRepository = messageRepository;
    }

    public CursorPageResponse<MessageResponse> getMessages(UUID userId, UUID chatId, UUID cursorId) {
        ensureUserInChat(userId, chatId);

        List<Message> messages;
        if (cursorId == null) {
            messages = messageRepository.findPartByChatId(chatId, MESSAGES_LIMIT + 1);
        } else {
            messages = messageRepository.findPartByChatIdAndCursor(chatId, cursorId, MESSAGES_LIMIT + 1);
        }
        return buildPageResponse(messages);
    }

    private CursorPageResponse<MessageResponse> buildPageResponse(List<Message> messages) {
        List<MessageResponse> responses = messages.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        UUID nextCursor = messages.isEmpty() ? null : messages.getLast().getId();
        boolean hasNext = messages.size() > MESSAGES_LIMIT;
        return new CursorPageResponse<>(responses, nextCursor, hasNext);
    }

    @Transactional
    public MessageCreationResult createMessage(UUID senderId, UUID chatId, MessageCreationRequest request) {
        ensureUserInChat(senderId, chatId);

        if (request.messageType() == MessageType.TEXT) {
            Message message = buildMessage(senderId, chatId, request);
            message = messageRepository.saveAndFlush(message);
            return buildResult(message);
        } else {
            // TODO: other message types
            return null;
        }
    }

    private MessageCreationResult buildResult(Message message) {
        List<String> recipientUsernames = chatApi.getChatParticipants(message.getChatId())
                .stream()
                .map(cm -> userApi.findUserById(cm.userId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(UserDto::username)
                .collect(Collectors.toList());
        MessageResponse messageResponse = mapToResponse(message);
        return new MessageCreationResult(messageResponse, recipientUsernames);
    }

    @Transactional
    public MessageCreationResult updateMessage(UUID userId, UUID messageId, MessageUpdateRequest request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message with id '%s' was not found".formatted(messageId)));
        if (!userId.equals(message.getSenderId())) {
            throw new MessageOwnershipException("The user with id '%s' is not the owner of the message with id '%s'".formatted(userId, message.getId()));
        }

        ensureUserInChat(userId, message.getChatId());

        message.setText(request.text());
        message = messageRepository.saveAndFlush(message);
        return buildResult(message);
    }

    @Transactional
    public MessageCreationResult deleteMessage(UUID userId, UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message with id '%s' was not found".formatted(messageId)));
        if (!userId.equals(message.getSenderId())) {
            throw new MessageOwnershipException("The user with id '%s' is not the owner of the message with id '%s'".formatted(userId, message.getId()));
        }

        ensureUserInChat(userId, message.getChatId());

        messageRepository.delete(message);
        return buildResult(message);
    }

    private void ensureUserInChat(UUID userId, UUID chatId) {
        if (!chatApi.isUserInChat(userId, chatId)) {
            throw new ChatMemberNotFoundException("The user with id '%s' is not a member of the chat with id '%s' or the chat is not exists".formatted(userId, chatId));
        }
    }

    private Message buildMessage(UUID userId, UUID chatId, MessageCreationRequest request) {
        return Message.builder()
                .chatId(chatId)
                .senderId(userId)
                .type(request.messageType())
                .text(request.text())
                .status(MessageStatus.SENT)
                .build();
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .chatId(message.getChatId())
                .senderId(message.getSenderId())
                .messageId(message.getId())
                .text(message.getText())
                .status(message.getStatus())
                .type(message.getType())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

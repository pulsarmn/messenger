package ru.pulsarmn.messenger.message.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.chat.domain.ChatMember;
import ru.pulsarmn.messenger.chat.domain.ChatMemberId;
import ru.pulsarmn.messenger.chat.exception.ChatMemberNotFoundException;
import ru.pulsarmn.messenger.chat.repository.ChatMemberRepository;
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
    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;

    private static final int MESSAGES_LIMIT = 50;

    public MessageService(UserApi userApi, MessageRepository messageRepository, ChatMemberRepository chatMemberRepository) {
        this.userApi = userApi;
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
    }

    public CursorPageResponse<MessageResponse> getMessages(UUID userId, UUID chatId, UUID cursorId) {
        ChatMemberId chatMemberId = new ChatMemberId(chatId, userId);
        chatMemberRepository.findById(chatMemberId)
                .orElseThrow(() -> new ChatMemberNotFoundException("The user with id '%s' is not a member of the chat with id '%s' or the chat is not exists".formatted(userId, chatId)));

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
        ChatMemberId chatMemberId = new ChatMemberId(chatId, senderId);
        ChatMember chatMember = chatMemberRepository.findById(chatMemberId)
                .orElseThrow(() -> new ChatMemberNotFoundException("The user with id '%s' is not a member of the chat with id '%s' or the chat is not exists".formatted(senderId, chatId)));

        if (request.messageType() == MessageType.TEXT) {
            Message message = buildMessage(chatMember, request);
            message = messageRepository.saveAndFlush(message);
            return buildResult(message);
        } else {
            // TODO: other message types
            return null;
        }
    }

    private MessageCreationResult buildResult(Message message) {
        List<ChatMember> chatMembers = chatMemberRepository.findAllByChatId(message.getChatId());
        MessageResponse messageResponse = mapToResponse(message);
        List<String> recipientUsernames = chatMembers.stream()
                .map(cm -> userApi.findUserById(cm.getUserId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(UserDto::username)
                .collect(Collectors.toList());
        return new MessageCreationResult(messageResponse, recipientUsernames);
    }

    @Transactional
    public MessageCreationResult updateMessage(UUID userId, UUID messageId, MessageUpdateRequest request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message with id '%s' was not found".formatted(messageId)));
        if (!userId.equals(message.getSenderId())) {
            throw new MessageOwnershipException("The user with id '%s' is not the owner of the message with id '%s'".formatted(userId, message.getId()));
        }

        UUID chatId = message.getChatId();
        ChatMemberId chatMemberId = new ChatMemberId(chatId, userId);
        chatMemberRepository.findById(chatMemberId)
                .orElseThrow(() -> new ChatMemberNotFoundException("The user with id '%s' is not a member of the chat with id '%s' or the chat is not exists".formatted(userId, chatId)));

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

        UUID chatId = message.getChatId();
        ChatMemberId chatMemberId = new ChatMemberId(chatId, userId);
        chatMemberRepository.findById(chatMemberId)
                .orElseThrow(() -> new ChatMemberNotFoundException("The user with id '%s' is not a member of the chat with id '%s' or the chat is not exists".formatted(userId, chatId)));

        messageRepository.delete(message);
        return buildResult(message);
    }

    private Message buildMessage(ChatMember chatMember, MessageCreationRequest request) {
        return Message.builder()
                .chatId(chatMember.getChat().getId())
                .senderId(chatMember.getUserId())
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

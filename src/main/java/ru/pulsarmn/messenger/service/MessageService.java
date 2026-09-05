package ru.pulsarmn.messenger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.dto.MessageCreationResult;
import ru.pulsarmn.messenger.dto.request.MessageCreationRequest;
import ru.pulsarmn.messenger.dto.response.MessageResponse;
import ru.pulsarmn.messenger.entity.*;
import ru.pulsarmn.messenger.exception.ChatMemberNotFoundException;
import ru.pulsarmn.messenger.repository.ChatMemberRepository;
import ru.pulsarmn.messenger.repository.MessageRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;

    public MessageService(MessageRepository messageRepository, ChatMemberRepository chatMemberRepository) {
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
    }

    @Transactional
    public MessageCreationResult createMessage(UUID senderId, UUID chatId, MessageCreationRequest request) {
        ChatMemberId chatMemberId = new ChatMemberId(chatId, senderId);
        ChatMember chatMember = chatMemberRepository.findById(chatMemberId)
                .orElseThrow(() -> new ChatMemberNotFoundException("The user with id '%s' is not a member of the chat with id '%s' or the chat is not exists".formatted(senderId, chatId)));

        if (request.messageType() == MessageType.TEXT) {
            Message message = buildMessage(chatMember, request);
            message = messageRepository.saveAndFlush(message);
            return buildResult(message, chatId);
        } else {
            // TODO: other message types
            return null;
        }
    }

    private MessageCreationResult buildResult(Message message, UUID chatId) {
        List<ChatMember> chatMembers = chatMemberRepository.findAllByChatId(chatId);
        MessageResponse messageResponse = mapToResponse(message, chatId);
        List<String> recipientUsernames = chatMembers.stream()
                .map(cm -> cm.getUser().getUsername())
                .collect(Collectors.toList());
        return new MessageCreationResult(messageResponse, recipientUsernames);
    }

    private Message buildMessage(ChatMember chatMember, MessageCreationRequest request) {
        return Message.builder()
                .chat(chatMember.getChat())
                .sender(chatMember.getUser())
                .type(request.messageType())
                .text(request.text())
                .status(MessageStatus.SENT)
                .build();
    }

    private MessageResponse mapToResponse(Message message, UUID chatId) {
        return MessageResponse.builder()
                .chatId(chatId)
                .senderId(message.getSender().getId())
                .messageId(message.getId())
                .text(message.getText())
                .status(message.getStatus())
                .type(message.getType())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

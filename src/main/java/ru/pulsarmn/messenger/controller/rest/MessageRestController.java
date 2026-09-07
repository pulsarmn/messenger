package ru.pulsarmn.messenger.controller.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pulsarmn.messenger.dto.MessageCreationResult;
import ru.pulsarmn.messenger.dto.MessageEvent;
import ru.pulsarmn.messenger.dto.request.MessageCreationRequest;
import ru.pulsarmn.messenger.dto.request.MessageUpdateRequest;
import ru.pulsarmn.messenger.security.UserPrincipal;
import ru.pulsarmn.messenger.service.MessageService;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class MessageRestController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String MESSAGES_TOPIC = "/queue/messages";

    public MessageRestController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/chats/{chatId}/messages")
    ResponseEntity<Void> createMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable UUID chatId,
                                       @Validated @RequestBody MessageCreationRequest request) {
        MessageCreationResult result = messageService.createMessage(userPrincipal.getUserId(), chatId, request);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.EventType.MESSAGE_CREATED, result.messageResponse());
        for (String username : result.recipientUsernames()) {
            messagingTemplate.convertAndSendToUser(username, MESSAGES_TOPIC, messageEvent);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/messages/{id}")
    ResponseEntity<Void> updateMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable UUID id,
                                       @Validated @RequestBody MessageUpdateRequest request) {
        MessageCreationResult result = messageService.updateMessage(userPrincipal.getUserId(), id, request);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.EventType.MESSAGE_UPDATED, result.messageResponse());
        for (String username : result.recipientUsernames()) {
            messagingTemplate.convertAndSendToUser(username, MESSAGES_TOPIC, messageEvent);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

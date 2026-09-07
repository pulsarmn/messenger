package ru.pulsarmn.messenger.message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pulsarmn.messenger.infrastructure.CursorPageResponse;
import ru.pulsarmn.messenger.infrastructure.UserPrincipal;

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

    @GetMapping("/{chatId}/messages")
    ResponseEntity<CursorPageResponse<MessageResponse>> getMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                    @PathVariable UUID chatId,
                                                                    @RequestParam(value = "cursor", required = false) UUID cursorId) {
        CursorPageResponse<MessageResponse> response = messageService.getMessages(userPrincipal.getUserId(), chatId, cursorId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
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
                                       @PathVariable("id") UUID messageId,
                                       @Validated @RequestBody MessageUpdateRequest request) {
        MessageCreationResult result = messageService.updateMessage(userPrincipal.getUserId(), messageId, request);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.EventType.MESSAGE_UPDATED, result.messageResponse());
        for (String username : result.recipientUsernames()) {
            messagingTemplate.convertAndSendToUser(username, MESSAGES_TOPIC, messageEvent);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/messages/{id}")
    ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable("id") UUID messageId) {
        MessageCreationResult result = messageService.deleteMessage(userPrincipal.getUserId(), messageId);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.EventType.MESSAGE_DELETED, result.messageResponse());
        for (String username : result.recipientUsernames()) {
            messagingTemplate.convertAndSendToUser(username, MESSAGES_TOPIC, messageEvent);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package ru.pulsarmn.messenger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pulsarmn.messenger.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.dto.request.MessageCreationRequest;
import ru.pulsarmn.messenger.dto.response.ChatResponse;
import ru.pulsarmn.messenger.dto.response.MessageResponse;
import ru.pulsarmn.messenger.security.UserPrincipal;
import ru.pulsarmn.messenger.service.DirectChatService;
import ru.pulsarmn.messenger.service.MessageService;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/chats")
public class ChatRestController {

    private final MessageService messageService;
    private final DirectChatService directChatService;

    public ChatRestController(MessageService messageService, DirectChatService directChatService) {
        this.messageService = messageService;
        this.directChatService = directChatService;
    }

    @PostMapping("/direct")
    ResponseEntity<ChatResponse> createChat(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChatCreationRequest request) {
        ChatResponse response = directChatService.getOrCreateDirectChat(userPrincipal.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{chatId}/messages")
    ResponseEntity<MessageResponse> sendMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable UUID chatId,
                                                @Validated @RequestBody MessageCreationRequest request) {
        MessageResponse response = messageService.createMessage(userPrincipal.getUserId(), chatId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

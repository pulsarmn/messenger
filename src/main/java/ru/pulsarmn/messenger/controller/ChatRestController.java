package ru.pulsarmn.messenger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.pulsarmn.messenger.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.security.UserPrincipal;
import ru.pulsarmn.messenger.service.DirectChatService;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/chats")
public class ChatRestController {

    private final DirectChatService directChatService;

    public ChatRestController(DirectChatService directChatService) {
        this.directChatService = directChatService;
    }

    @PostMapping("/direct")
    ResponseEntity<UUID> createChat(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChatCreationRequest request) {
        UUID chatId = directChatService.createChat(userPrincipal.getUserId(), request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatId);
    }
}

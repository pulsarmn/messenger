package ru.pulsarmn.messenger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsarmn.messenger.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.dto.response.ChatResponse;
import ru.pulsarmn.messenger.security.UserPrincipal;
import ru.pulsarmn.messenger.service.ChatProcessor;


@RestController
@RequestMapping("/api/v1/chats")
public class ChatRestController {

    private final ChatProcessor chatProcessor;

    public ChatRestController(ChatProcessor chatProcessor) {
        this.chatProcessor = chatProcessor;
    }

    @PostMapping("/direct")
    ResponseEntity<ChatResponse> createChat(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChatCreationRequest request) {
        ChatResponse response = chatProcessor.createChat(userPrincipal.getUserId(), request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}

package ru.pulsarmn.messenger.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.pulsarmn.messenger.dto.request.ChatCreationRequest;
import ru.pulsarmn.messenger.dto.response.ChatResponse;
import ru.pulsarmn.messenger.dto.response.CursorPageResponse;
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

    @GetMapping("/{chatId}/messages")
    ResponseEntity<CursorPageResponse<MessageResponse>> getMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                    @PathVariable UUID chatId,
                                                                    @RequestParam(value = "cursor", required = false) UUID cursorId) {
        CursorPageResponse<MessageResponse> response = messageService.getMessages(userPrincipal.getUserId(), chatId, cursorId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}

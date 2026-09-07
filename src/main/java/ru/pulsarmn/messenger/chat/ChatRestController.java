package ru.pulsarmn.messenger.chat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsarmn.messenger.infrastructure.UserPrincipal;


@RestController
@RequestMapping("/api/v1/chats")
public class ChatRestController {

    private final DirectChatService directChatService;

    public ChatRestController(DirectChatService directChatService) {
        this.directChatService = directChatService;
    }

    @PostMapping("/direct")
    ResponseEntity<ChatResponse> createChat(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChatCreationRequest request) {
        ChatResponse response = directChatService.getOrCreateDirectChat(userPrincipal.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}

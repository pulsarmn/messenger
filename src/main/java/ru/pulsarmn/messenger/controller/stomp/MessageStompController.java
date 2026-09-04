package ru.pulsarmn.messenger.controller.stomp;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import ru.pulsarmn.messenger.dto.request.MessageCreationRequest;
import ru.pulsarmn.messenger.security.UserPrincipal;
import ru.pulsarmn.messenger.service.MessageService;

import java.util.UUID;


@Controller
public class MessageStompController {

    private final MessageService messageService;

    public MessageStompController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chats/{chatId}/send-message")
    void handleNewMessage(@DestinationVariable UUID chatId, Authentication authentication, MessageCreationRequest request) {
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            messageService.createMessage(userPrincipal.getUserId(), chatId, request);
        }
    }
}

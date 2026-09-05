package ru.pulsarmn.messenger.controller.stomp;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import ru.pulsarmn.messenger.dto.MessageCreationResult;
import ru.pulsarmn.messenger.dto.request.MessageCreationRequest;
import ru.pulsarmn.messenger.security.UserPrincipal;
import ru.pulsarmn.messenger.service.MessageService;

import java.util.UUID;


@Controller
public class MessageStompController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String MESSAGES_PREFIX = "/queue/messages";

    public MessageStompController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chats/{chatId}/send-message")
    void handleNewMessage(@DestinationVariable UUID chatId, Authentication authentication, MessageCreationRequest request) {
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            MessageCreationResult result = messageService.createMessage(userPrincipal.getUserId(), chatId, request);
            for (String username : result.recipientUsernames()) {
                messagingTemplate.convertAndSendToUser(username, MESSAGES_PREFIX, result.messageResponse());
            }
        }
    }
}

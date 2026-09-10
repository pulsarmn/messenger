package ru.pulsarmn.messenger.message.dto;

import ru.pulsarmn.messenger.message.dto.response.MessageResponse;

import java.util.List;


public record MessageCreationResult(MessageResponse messageResponse,
                                    List<String> recipientUsernames) {
}

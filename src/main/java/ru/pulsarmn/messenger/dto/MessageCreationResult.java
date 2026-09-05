package ru.pulsarmn.messenger.dto;

import ru.pulsarmn.messenger.dto.response.MessageResponse;

import java.util.List;


public record MessageCreationResult(MessageResponse messageResponse,
                                    List<String> recipientUsernames) {
}

package ru.pulsarmn.messenger.message;

import java.util.List;


public record MessageCreationResult(MessageResponse messageResponse,
                                    List<String> recipientUsernames) {
}

package ru.pulsarmn.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.pulsarmn.messenger.entity.MessageType;


public record MessageCreationRequest(

        @NotNull
        MessageType messageType,

        @NotBlank
        @Size(max = 4096)
        String text
) {
}

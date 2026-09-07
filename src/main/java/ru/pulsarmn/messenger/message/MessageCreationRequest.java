package ru.pulsarmn.messenger.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record MessageCreationRequest(

        @NotNull
        MessageType messageType,

        @NotBlank
        @Size(max = 4096)
        String text
) {
}

package ru.pulsarmn.messenger.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record MessageUpdateRequest(

        @NotBlank
        @Size(max = 4096)
        String text
) {
}

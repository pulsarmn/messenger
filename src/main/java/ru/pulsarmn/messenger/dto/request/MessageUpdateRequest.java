package ru.pulsarmn.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record MessageUpdateRequest(

        @NotBlank
        @Size(max = 4096)
        String text
) {
}

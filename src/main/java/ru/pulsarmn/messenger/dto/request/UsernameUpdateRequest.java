package ru.pulsarmn.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UsernameUpdateRequest(

        @NotBlank
        @Size(min = 4, max = 32)
        String newUsername
) {
}

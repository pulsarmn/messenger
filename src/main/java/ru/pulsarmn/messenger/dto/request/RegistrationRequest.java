package ru.pulsarmn.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegistrationRequest(

        @NotBlank
        @Size(min = 4, max = 32)
        String username,

        @NotBlank
        @Size(min = 8, max = 64)
        String password,

        @NotBlank
        @Size(min = 8, max = 64)
        String passwordConfirmation
) {
}

package org.pulsar.messenger.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegistrationRequest(

        @NotBlank
        @Size(min = 2, max = 128)
        String username,

        @NotBlank
        String password,

        @NotBlank
        String passwordConfirmation) {
}

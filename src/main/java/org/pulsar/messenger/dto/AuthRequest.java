package org.pulsar.messenger.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record AuthRequest(

        @NotBlank
        @Size(min = 2, max = 128)
        String username,

        @NotBlank
        @Size(min = 8, max = 128)
        String password) {
}

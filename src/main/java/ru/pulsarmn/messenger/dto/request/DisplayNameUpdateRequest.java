package ru.pulsarmn.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record DisplayNameUpdateRequest(

        @NotBlank
        @Size(min = 1, max = 64)
        String newDisplayName
) {
}

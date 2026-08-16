package ru.pulsarmn.messenger.dto.response;

import java.time.LocalDate;


public record UserProfileResponse(
        String username,
        String displayName,
        String phoneNumber,
        LocalDate birthdate
) {
}

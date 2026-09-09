package ru.pulsarmn.messenger.user.internal.dto.response;

import java.time.LocalDate;


public record UserProfileResponse(
        String username,
        String displayName,
        String phoneNumber,
        LocalDate birthdate
) {
}

package ru.pulsarmn.messenger.user.api.dto.request;

import java.time.LocalDate;


public record BirthdateUpdateRequest(LocalDate newBirthDate) {
}

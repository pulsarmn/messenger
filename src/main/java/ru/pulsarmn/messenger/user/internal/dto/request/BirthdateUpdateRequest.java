package ru.pulsarmn.messenger.user.internal.dto.request;

import java.time.LocalDate;


public record BirthdateUpdateRequest(LocalDate newBirthDate) {
}

package ru.pulsarmn.messenger.dto.request;

import java.time.LocalDate;


public record BirthdateUpdateRequest(LocalDate newBirthDate) {
}

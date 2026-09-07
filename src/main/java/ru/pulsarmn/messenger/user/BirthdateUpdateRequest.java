package ru.pulsarmn.messenger.user;

import java.time.LocalDate;


public record BirthdateUpdateRequest(LocalDate newBirthDate) {
}

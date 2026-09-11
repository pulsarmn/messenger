package ru.pulsarmn.messenger.user.api.dto.request;

import java.time.LocalDate;


public record UserCreateRequest(String username,
                                String passwordHash,
                                String phoneNumber,
                                String displayName,
                                LocalDate birthdate) {
}

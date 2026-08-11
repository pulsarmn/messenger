package ru.pulsarmn.messenger.security;

import java.util.UUID;


public record UserPrincipal(
        UUID id,
        String username
) {
}

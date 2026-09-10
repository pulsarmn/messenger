package ru.pulsarmn.messenger.infrastructure.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pulsarmn.messenger.user.api.dto.response.UserDto;

import java.util.Collection;
import java.util.List;


public class SecurityUser implements UserDetails {

    private final UserDto user;

    public SecurityUser(UserDto user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.username();
    }

    @Override
    public @Nullable String getPassword() {
        return user.passwordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}

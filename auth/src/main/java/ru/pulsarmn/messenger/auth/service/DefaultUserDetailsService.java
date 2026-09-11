package ru.pulsarmn.messenger.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pulsarmn.messenger.infrastructure.security.SecurityUser;
import ru.pulsarmn.messenger.user.api.UserApi;


@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserApi userApi;

    public DefaultUserDetailsService(UserApi userApi) {
        this.userApi = userApi;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userApi.findUserByUsername(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '%s' not found".formatted(username)));
    }
}

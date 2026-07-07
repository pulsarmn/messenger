package org.pulsar.messenger.mapper;

import org.pulsar.messenger.dto.RegistrationRequest;
import org.pulsar.messenger.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public User mapToUser(RegistrationRequest registrationRequest, String encodedPassword) {
        return User.builder()
                .username(registrationRequest.username())
                .displayName(registrationRequest.username())
                .passwordHash(encodedPassword)
                .build();
    }
}

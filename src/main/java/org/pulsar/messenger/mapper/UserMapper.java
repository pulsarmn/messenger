package org.pulsar.messenger.mapper;

import org.pulsar.messenger.dto.request.RegistrationRequest;
import org.pulsar.messenger.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public User mapToEntity(RegistrationRequest request, String encodedPassword) {
        return User.builder()
                .username(request.username())
                .displayName(request.username())
                .passwordHash(encodedPassword)
                .build();
    }
}

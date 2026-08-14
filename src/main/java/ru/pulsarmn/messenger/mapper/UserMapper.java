package ru.pulsarmn.messenger.mapper;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.dto.request.RegistrationRequest;
import ru.pulsarmn.messenger.dto.response.UserProfileResponse;
import ru.pulsarmn.messenger.dto.response.UserSearchResponse;
import ru.pulsarmn.messenger.entity.User;


@Component
public class UserMapper {

    public User mapToEntity(RegistrationRequest request, String passwordHash) {
        return User.builder()
                .username(request.username())
                .passwordHash(passwordHash)
                .displayName("Default name") // TODO: extract this logic
                .build();
    }

    public UserSearchResponse mapToSearchResponse(User user) {
        return new UserSearchResponse(user.getUsername());
    }
}

package ru.pulsarmn.messenger.auth;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.user.api.dto.request.UserCreateRequest;


@Component
public class AuthUserMapper {

    private static final String DEFAULT_DISPLAY_NAME = "Default name";

    public UserCreateRequest mapToCreateRequest(RegistrationRequest request, String passwordHash) {
        // TODO: refactor this method
        return new UserCreateRequest(request.username(), passwordHash, null, DEFAULT_DISPLAY_NAME, null);
    }
}

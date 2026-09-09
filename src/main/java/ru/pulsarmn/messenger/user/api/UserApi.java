package ru.pulsarmn.messenger.user.api;

import ru.pulsarmn.messenger.user.api.dto.request.UserCreateRequest;
import ru.pulsarmn.messenger.user.api.dto.response.UserResponse;

import java.util.Optional;


public interface UserApi {

    UserResponse createUser(UserCreateRequest request);

    Optional<UserResponse> findAuthDataByUsername(String username);
}

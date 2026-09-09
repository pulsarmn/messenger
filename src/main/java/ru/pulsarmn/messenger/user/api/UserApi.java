package ru.pulsarmn.messenger.user.api;

import ru.pulsarmn.messenger.user.api.dto.request.UserCreateRequest;
import ru.pulsarmn.messenger.user.api.dto.response.UserResponse;

import java.util.Optional;
import java.util.UUID;


public interface UserApi {

    UserResponse createUser(UserCreateRequest request);

    Optional<UserResponse> findUserById(UUID userId);

    Optional<UserResponse> findUserByUsername(String username);
}

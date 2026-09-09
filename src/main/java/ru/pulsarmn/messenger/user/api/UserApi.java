package ru.pulsarmn.messenger.user.api;

import ru.pulsarmn.messenger.user.api.dto.request.UserCreateRequest;
import ru.pulsarmn.messenger.user.api.dto.response.UserDto;

import java.util.Optional;
import java.util.UUID;


public interface UserApi {

    UserDto createUser(UserCreateRequest request);

    Optional<UserDto> findUserById(UUID userId);

    Optional<UserDto> findUserByUsername(String username);
}

package ru.pulsarmn.messenger.user.internal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.user.api.UserApi;
import ru.pulsarmn.messenger.user.api.dto.request.UserCreateRequest;
import ru.pulsarmn.messenger.user.api.dto.response.UserResponse;
import ru.pulsarmn.messenger.user.internal.domain.User;
import ru.pulsarmn.messenger.user.internal.mapper.UserMapper;
import ru.pulsarmn.messenger.user.internal.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;


@Component
public class DefaultUserApi implements UserApi  {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public DefaultUserApi(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.mapToUser(request);
        user = userRepository.saveAndFlush(user);
        return userMapper.mapToResponse(user);
    }

    @Override
    public Optional<UserResponse> findUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapToResponse);
    }

    @Override
    public Optional<UserResponse> findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::mapToResponse);
    }
}

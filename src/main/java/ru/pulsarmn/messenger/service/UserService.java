package ru.pulsarmn.messenger.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.dto.request.BirthdateUpdateRequest;
import ru.pulsarmn.messenger.dto.request.DisplayNameUpdateRequest;
import ru.pulsarmn.messenger.dto.request.UsernameUpdateRequest;
import ru.pulsarmn.messenger.dto.response.UserProfileResponse;
import ru.pulsarmn.messenger.dto.response.UserSearchResponse;
import ru.pulsarmn.messenger.entity.User;
import ru.pulsarmn.messenger.exception.UserNotFoundException;
import ru.pulsarmn.messenger.mapper.UserMapper;
import ru.pulsarmn.messenger.repository.UserRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;


@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public Page<UserSearchResponse> findUsers(String query, Pageable pageable) {
        return userRepository.searchUsers(query, pageable)
                .map(userMapper::mapToSearchResponse);
    }

    public UserProfileResponse getUserProfile(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapToProfileResponse)
                .orElseThrow(() -> createUserNotFoundException(userId));
    }

    private UserNotFoundException createUserNotFoundException(UUID userId) {
        return new UserNotFoundException("User with id '%s' not found".formatted(userId));
    }
}

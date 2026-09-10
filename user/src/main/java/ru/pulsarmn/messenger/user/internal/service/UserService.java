package ru.pulsarmn.messenger.user.internal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.user.api.exception.UserNotFoundException;
import ru.pulsarmn.messenger.user.internal.domain.User;
import ru.pulsarmn.messenger.user.internal.dto.request.BirthdateUpdateRequest;
import ru.pulsarmn.messenger.user.internal.dto.request.DisplayNameUpdateRequest;
import ru.pulsarmn.messenger.user.internal.dto.request.UsernameUpdateRequest;
import ru.pulsarmn.messenger.user.internal.dto.response.UserProfileResponse;
import ru.pulsarmn.messenger.user.internal.dto.response.UserSearchResponse;
import ru.pulsarmn.messenger.user.internal.mapper.UserMapper;
import ru.pulsarmn.messenger.user.internal.repository.UserRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;


@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> createUserNotFoundException(userId));
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

    @Transactional
    public UserProfileResponse updateUsername(UUID userId, UsernameUpdateRequest request) {
        return update(userId, user -> setNewUsernameIfNecessary(user, request));
    }

    private User setNewUsernameIfNecessary(User user, UsernameUpdateRequest request) {
        String newUsername = request.newUsername();
        if (!(user.getUsername()).equals(newUsername)) {
            user.setUsername(newUsername);
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public UserProfileResponse updateDisplayName(UUID userId, DisplayNameUpdateRequest request) {
        return update(userId, user -> setNewDisplayNameIfNecessary(user, request));
    }

    private User setNewDisplayNameIfNecessary(User user, DisplayNameUpdateRequest request) {
        String newDisplayName = request.newDisplayName();
        if (!(user.getDisplayName()).equals(newDisplayName)) {
            user.setDisplayName(newDisplayName);
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public UserProfileResponse updateBirthdate(UUID userId, BirthdateUpdateRequest request) {
        return update(userId, user -> setNewBirthdateIfNecessary(user, request));
    }

    private User setNewBirthdateIfNecessary(User user, BirthdateUpdateRequest request) {
        // TODO: add birthdate validation
        LocalDate newBirthdate = request.newBirthDate();
        if (!Objects.equals(user.getBirthdate(), newBirthdate)) {
            user.setBirthdate(newBirthdate);
            userRepository.save(user);
        }
        return user;
    }

    private UserProfileResponse update(UUID userId, Function<User, User> updateFunction) {
        return userRepository.findById(userId)
                .map(updateFunction)
                .map(userMapper::mapToProfileResponse)
                .orElseThrow(() -> createUserNotFoundException(userId));
    }

    private UserNotFoundException createUserNotFoundException(UUID userId) {
        return new UserNotFoundException("User with id '%s' not found".formatted(userId));
    }
}

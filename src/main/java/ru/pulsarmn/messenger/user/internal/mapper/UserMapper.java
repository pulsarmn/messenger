package ru.pulsarmn.messenger.user.internal.mapper;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.user.api.dto.request.UserCreateRequest;
import ru.pulsarmn.messenger.user.api.dto.response.UserDto;
import ru.pulsarmn.messenger.user.internal.domain.User;
import ru.pulsarmn.messenger.user.internal.dto.response.UserProfileResponse;
import ru.pulsarmn.messenger.user.internal.dto.response.UserSearchResponse;


@Component
public class UserMapper {

    public UserSearchResponse mapToSearchResponse(User user) {
        return new UserSearchResponse(user.getUsername());
    }

    public UserProfileResponse mapToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getUsername(),
                user.getDisplayName(),
                user.getPhoneNumber(),
                user.getBirthdate()
        );
    }

    public User mapToUser(UserCreateRequest request) {
        return User.builder()
                .username(request.username())
                .passwordHash(request.passwordHash())
                .phoneNumber(request.phoneNumber())
                .displayName(request.displayName())
                .birthdate(request.birthdate())
                .build();
    }

    public UserDto mapToResponse(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .passwordHash(user.getPasswordHash())
                .phoneNumber(user.getPasswordHash())
                .displayName(user.getDisplayName())
                .birthdate(user.getBirthdate())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

package ru.pulsarmn.messenger.user;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.auth.RegistrationRequest;


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

    public UserProfileResponse mapToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getUsername(),
                user.getDisplayName(),
                user.getPhoneNumber(),
                user.getBirthdate()
        );
    }
}

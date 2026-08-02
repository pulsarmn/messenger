package ru.pulsarmn.messenger.mapper;


import org.junit.jupiter.api.Test;
import ru.pulsarmn.messenger.dto.request.RegistrationRequest;
import ru.pulsarmn.messenger.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void mapToEntity_whenCorrectRequest_shouldMapItToUser() {
        RegistrationRequest request = new RegistrationRequest("correct-username", "correct-password", "correct-password");
        String encodedPassword = "correct_encoded_password";

        User actualUser = userMapper.mapToEntity(request, encodedPassword);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getUsername()).isEqualTo(request.username());
        assertThat(actualUser.getPasswordHash()).isEqualTo(encodedPassword);
    }

    @Test
    void mapToEntity_whenNullRequest_shouldThrownNullPointerException() {
        assertThatThrownBy(() -> userMapper.mapToEntity(null, "correct_encoded_password"))
                .isInstanceOf(NullPointerException.class);
    }
}

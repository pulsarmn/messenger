package org.pulsar.messenger.mapper;


import org.junit.jupiter.api.Test;
import org.pulsar.messenger.dto.request.RegistrationRequest;
import org.pulsar.messenger.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void mapToEntity_whenCorrectArguments_shouldReturnMappedEntity() {
        String username = "correct_username";
        String password = "12345678";
        String passwordHash = "correct_password_hash";
        RegistrationRequest request = new RegistrationRequest(username, password, password);

        User user = userMapper.mapToEntity(request, passwordHash);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getDisplayName()).isEqualTo(username);
        assertThat(user.getPasswordHash()).isEqualTo(passwordHash);
    }

    @Test
    void mapToEntity_whenRequestIsNull_shouldThrowNullPointerException() {
        RegistrationRequest request = null;
        String correctPasswordHash = "correct_password_hash";

        assertThatThrownBy(() -> userMapper.mapToEntity(request, correctPasswordHash));
    }
}

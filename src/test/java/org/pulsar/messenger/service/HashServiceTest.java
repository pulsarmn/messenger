package org.pulsar.messenger.service;


import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class HashServiceTest {

    private final HashService hashService = new HashService();

    @Test
    void hash_whenNonNullBytesArray_shouldReturnHexHash() {
        String strRefreshToken = "38f30bb3-b5bd-471e-9db4-708d9e9caf79";
        byte[] refreshTokenBytes = strRefreshToken.getBytes(StandardCharsets.UTF_8);
        String expectedHash = "414d4a1282f03e87ca3da5ed231e00f53895d61857f424924b5e502531081f45";

        String actualHash = hashService.hash(refreshTokenBytes);

        assertThat(actualHash).isEqualTo(expectedHash);
    }

    @Test
    void hash_whenEmptyBytesArray_shouldReturnHashOfEmptyString() {
        byte[] emptyBytes = new byte[0];
        String expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        String actualHash = hashService.hash(emptyBytes);

        assertThat(actualHash).isEqualTo(expectedHash);
    }

    @Test
    void hash_whenBytesArrayIsNull_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> hashService.hash(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Bytes array must not be null");
    }
}

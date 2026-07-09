package org.pulsar.messenger.service;


import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class TokenGeneratorTest {

    private final TokenGenerator tokenGenerator = new TokenGenerator();

    @ParameterizedTest
    @ValueSource(ints = {2, 8, 16, 32})
    void generate_whenPositiveLength_shouldReturnArrayWithRandomBytes(int length) {
        byte[] bytes = tokenGenerator.generate(length);

        assertThat(bytes).hasSize(length);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void generate_whenLengthIsZeroOrNegative_shouldReturnEmptyBytesArray(int length) {
        byte[] bytes = tokenGenerator.generate(length);

        assertThat(bytes).isEmpty();
    }
}

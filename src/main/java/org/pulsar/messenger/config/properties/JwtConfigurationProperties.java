package org.pulsar.messenger.config.properties;


import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties("app.jwt")
public record JwtConfigurationProperties(

        @NotBlank
        String privateKey,

        @NotBlank
        String publicKey
) {
}

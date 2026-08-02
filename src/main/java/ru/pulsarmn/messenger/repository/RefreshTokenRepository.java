package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<RefreshToken> findByTokenHash(String refreshTokenHash);
}

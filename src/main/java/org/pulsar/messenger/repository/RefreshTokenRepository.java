package org.pulsar.messenger.repository;

import org.pulsar.messenger.entity.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @EntityGraph(attributePaths = {"user"})
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}

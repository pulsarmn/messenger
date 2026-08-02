package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.entity.RefreshToken;

import java.util.UUID;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
}

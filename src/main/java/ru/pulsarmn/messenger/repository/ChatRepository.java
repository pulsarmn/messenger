package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.entity.Chat;

import java.util.UUID;


public interface ChatRepository extends JpaRepository<Chat, UUID> {
}

package ru.pulsarmn.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.chat.domain.Chat;

import java.util.UUID;


public interface ChatRepository extends JpaRepository<Chat, UUID> {
}

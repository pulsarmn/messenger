package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.entity.Message;

import java.util.UUID;


public interface MessageRepository extends JpaRepository<Message, UUID> {
}

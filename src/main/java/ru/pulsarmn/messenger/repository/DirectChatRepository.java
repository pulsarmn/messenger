package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.entity.DirectChat;
import ru.pulsarmn.messenger.entity.DirectChatId;


public interface DirectChatRepository extends JpaRepository<DirectChat, DirectChatId> {
}

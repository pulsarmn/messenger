package ru.pulsarmn.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.chat.domain.DirectChat;
import ru.pulsarmn.messenger.chat.domain.DirectChatId;


public interface DirectChatRepository extends JpaRepository<DirectChat, DirectChatId> {
}

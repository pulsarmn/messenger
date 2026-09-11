package ru.pulsarmn.messenger.chat.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.chat.internal.domain.DirectChat;
import ru.pulsarmn.messenger.chat.internal.domain.DirectChatId;


public interface DirectChatRepository extends JpaRepository<DirectChat, DirectChatId> {
}

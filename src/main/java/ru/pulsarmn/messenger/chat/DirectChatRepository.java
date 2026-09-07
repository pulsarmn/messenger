package ru.pulsarmn.messenger.chat;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DirectChatRepository extends JpaRepository<DirectChat, DirectChatId> {
}

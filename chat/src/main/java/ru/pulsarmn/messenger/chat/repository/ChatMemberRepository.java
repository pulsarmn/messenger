package ru.pulsarmn.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.chat.domain.ChatMember;
import ru.pulsarmn.messenger.chat.domain.ChatMemberId;

import java.util.List;
import java.util.UUID;


public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId> {

    List<ChatMember> findAllByChatId(UUID chatId);
}

package ru.pulsarmn.messenger.chat.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.chat.internal.domain.ChatMember;
import ru.pulsarmn.messenger.chat.internal.domain.ChatMemberId;

import java.util.List;
import java.util.UUID;


public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId> {

    List<ChatMember> findAllByChatId(UUID chatId);
}

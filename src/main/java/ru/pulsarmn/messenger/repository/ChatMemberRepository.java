package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;
import ru.pulsarmn.messenger.entity.ChatMember;
import ru.pulsarmn.messenger.entity.ChatMemberId;

import java.util.List;
import java.util.UUID;


public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId> {

    List<ChatMember> findAllByChatId(UUID chatId);
}

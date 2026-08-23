package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsarmn.messenger.entity.ChatMember;
import ru.pulsarmn.messenger.entity.ChatMemberId;


public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId> {
}

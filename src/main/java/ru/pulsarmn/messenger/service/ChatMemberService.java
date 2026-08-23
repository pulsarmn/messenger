package ru.pulsarmn.messenger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.entity.ChatMember;
import ru.pulsarmn.messenger.repository.ChatMemberRepository;


@Service
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;

    public ChatMemberService(ChatMemberRepository chatMemberRepository) {
        this.chatMemberRepository = chatMemberRepository;
    }

    @Transactional
    public ChatMember save(ChatMember chatMember) {
        return chatMemberRepository.save(chatMember);
    }
}

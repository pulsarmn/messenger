package ru.pulsarmn.messenger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.entity.DirectChat;
import ru.pulsarmn.messenger.entity.DirectChatId;
import ru.pulsarmn.messenger.repository.DirectChatRepository;

import java.util.Optional;


@Service
public class DirectChatService {

    private final DirectChatRepository directChatRepository;

    public DirectChatService(DirectChatRepository directChatRepository) {
        this.directChatRepository = directChatRepository;
    }

    public Optional<DirectChat> findById(DirectChatId id) {
        return directChatRepository.findById(id);
    }

    @Transactional
    public DirectChat save(DirectChat directChat) {
        return directChatRepository.save(directChat);
    }
}

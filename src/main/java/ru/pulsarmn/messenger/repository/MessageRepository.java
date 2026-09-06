package ru.pulsarmn.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pulsarmn.messenger.entity.Message;

import java.util.List;
import java.util.UUID;


public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query(value = """
                SELECT * FROM messages
                WHERE chat_id = :chatId AND created_at < (SELECT created_at FROM messages WHERE id = :cursorId)
                ORDER BY created_at DESC
                LIMIT :limit
            """, nativeQuery = true)
    List<Message> findPartByChatIdAndCursor(UUID chatId, UUID cursorId, int limit);

    @Query(value = """
                SELECT * FROM messages
                WHERE chat_id = :chatId
                ORDER BY created_at DESC
                LIMIT :limit
            """, nativeQuery = true)
    List<Message> findPartByChatId(UUID chatId, int limit);
}

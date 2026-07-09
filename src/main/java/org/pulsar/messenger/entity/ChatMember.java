package org.pulsar.messenger.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@ToString(exclude = {"chat", "user"})
@EqualsAndHashCode(exclude = {"chat", "user"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "chat_members")
public class ChatMember {

    @EmbeddedId
    private ChatMemberId id;

    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Chat chat;

    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}

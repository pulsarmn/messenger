package org.pulsar.messenger.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;


@Data
@Builder
@ToString(exclude = {"sender", "chat"})
@EqualsAndHashCode(exclude = {"sender", "chat"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "chat_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Chat chat;

    @JoinColumn(name = "sender_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User sender;

    @Column(name = "content")
    private String content;

    @Builder.Default
    @Column(name = "is_read")
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}

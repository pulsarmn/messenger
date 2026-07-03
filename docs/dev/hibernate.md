# 2026-07-03 | Composite ID
- - -
Бывают ситуации, когда нельзя обойтись обычным первичным ключом и нужно чтобы идентификатор состоял из 2 и более значений. Лучше не допускать таких ситуаций, но если всё-таки нужно реализовать логику таким образом, то нужно реализовать составной первичный ключ.

Для этого нужно создать отдельный класс, например, **ChatMemberId**:

```jav@Data
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
}a
@Getter
@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChatMemberId {

    @Column(name = "chat_id")
    private UUID chatId;

    @Column(name = "user_id")
    private UUID userId;
}
```

Здесь указываются значения, которые используются для формирования первичного ключа. Важно понимать, что значения не должны быть сущностями(например, если используется их id для формирования первичного ключа), а должны использоваться обычные типы: Integer, Long, String, UUID, etc.

Также нужно установить аннотацию **@Embeddable** над этим классом, чтобы **Hibernate** понимал, что это встраиваемый объект.

После этого можно использовать данный класс в сущности:

```java
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
```

Здесь прикол в том, что если я оставлю только аннотации **@JoinColumn** и **@ManyToOne**, то при запуске **Hibernate** упадёт с ошибкой, так как происходит дублирование. Ты указал колонки как в составном ключе, так и в самой сущности, поэтому **Hibernate** не понимает, что использовать.

Чтобы избавиться от этой ошибки, нужно использовать аннотацию **@MapsId**. Она обычно используется в связке с аннотацией **@EmbeddedId**. Внутри неё указывает имя поля составного ключа, где уже указана аннотация **@Column**, чтобы избежать дублирования.

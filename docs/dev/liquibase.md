# 2026-07-02 | Changeset structure
- - -
Файлы миграции могут быть описаны с помощью разных форматов: SQL, JSON, XML, YAML. Обычно лучше всего использовать миграции в формате **SQL**, так как данное решение будет более читаемое.

Любой **changeset** файл начинается со следующей директивы:

```sql
--liquibase formatted sql
```

Это строка говорит, что данный файл использует именно формат **SQL**, а не какой-то другой.

Далее идут строки следующего вида:

```sql
--changeset <author>:<id>
```

- `author` - обычно это имя, почта или ник разработчика, который сделал эту миграцию
- `id` - уникальный идентификатор `changeset'a`. Должен быть уникальным в рамках одного файла. Может быть любое значение, будь то число или строка. Тут всё зависит от того, какой стратегии придерживаются в команде.

Данная строка позволяет делать атомарные накаты миграций, чтобы потом можно было откатить только необходимый кусочек, а не всё сразу.

```sql
--liquibase formatted sql

--changeset pulsarmn:create-users-table
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

--changeset pulsarmn:add-phone-column
ALTER TABLE users ADD COLUMN phone VARCHAR(20);
```

К тому же у директивы `--changeset` есть множество необязательных параметров:

- `runAlways` - означает, что миграция будет выполняться каждый раз при новом запуске, даже если она уже многократно выполнялась. Может быть полезно для удаления старых данных, обновления статистики и т.д.

```sql
--changeset pulsarmn:update-statistics runAlways:true
UPDATE statistics SET last_updated = NOW();
```

- `runOnChange` - миграция будет выполнена только в том случае, если данные поменялись после последнего запуска.

```sql
--changeset pulsarmn:update-config runOnChange:true
UPDATE config SET value = 'new_value' WHERE key = 'setting';
```

- `splitStatements` - по умолчанию **Liquibase** разбивает **SQL** по `;`. Из-за этого хранимые процедуры могут работать некорректно. Чтобы это исправить, нужно установить данный параметр в **false**

```sql
--changeset pulsarmn:complex-procedure splitStatements:false
CREATE OR REPLACE PROCEDURE update_users()
BEGIN
    -- сложный PL/SQL код
END;
```

- `endDelimiter` - указывает конечный разделитель (вместо `;`). Может быть полезно для сложных структур с множеством `;`

```sql
--changeset pulsarmn:create-trigger endDelimiter:/
CREATE TRIGGER before_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    SET NEW.created_at = NOW();
END;
/
```

- `stripComments` - удаляет комментарии перед выполнением кода. Обычно не нужно

```sql
--changeset pulsarmn:data-migration stripComments:true
-- Это комментарий, который будет удален перед выполнением
INSERT INTO users (username) VALUES ('test');
```

- `dbms` - указывает БД, на которой должны выполняться миграции. Редко используется

```sql
--changeset pulsarmn:postgres-specific dbms:postgresql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

- `context` - миграция будет выполнена только в том случае, если указан определённый контекст при запуске. Может быть полезно для тестирования

```sql
--changeset pulsarmn:test-data context:test
INSERT INTO users (username) VALUES ('test_user');
```

- `runInTransaction` - по умолчанию установлен в **true** и выполняет **changeset** в транзакции. Если такое поведение не устраивает(например нужно асинхронно создать индекс), то нужно установить его в **false**

```sql
--changeset john.doe:create-index runInTransaction:false
CREATE INDEX CONCURRENTLY idx_users_email ON users(email);
```

Также есть директива, позволяющая откатывать изменения:

```sql
--changeset john.doe:add-column
ALTER TABLE users ADD COLUMN age INT;

--rollback ALTER TABLE users DROP COLUMN age;
```

К тому же `--rollback` автоматически работает для операций типа **CREATE**, но не работает для операций **DROP**, **UPDATE** и **DELETE**.

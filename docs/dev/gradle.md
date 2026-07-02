# 2026-07-01 | Gradle Files
- - -
Ты можешь настраивать Gradle на 3 уровнях:

- **init.gradle** - настройки в данном файле применяются ко всем проектам, собираемые Gradle на данном хосте. Вообще не рекомендуется его использовать в повседневной практике, но знать об этом нужно, так как могут возникнуть ситуации, когда данный вид настройки будет полезен.
- **settings.gradle** - данный файл уже находится в конкретном проекте и позволяет настраивать сборку многомодульных проектов. Особенно полезен в модульных монолитах и микросервисных проектах, где используется моно-репозиторий.
- **build.gradle** - а данный файл определяет, как будет собираться каждый отдельный модуль. Если проект состоит из 1 модуля(самого себя), то в корне будет лежать просто 1 **build.gradle** файл и всё. Если же используется многомодульный проект, то в каждом модуле будет лежать собственный **build.gradle** файл.


# 2026-07-01 | build.gradle
- - -
Обычно при использовании Gradle, ты используешь удобный **DSL**, который позволяет очень лаконично настраивать проект. Почти никто не задумывается о том, как это всё устроено внутри. Да, это может и не обязательно нужно понимать, чтобы суметь настроить Gradle, но если возникнет более сложная ситуация, то тут без знания того, как Gradle устроен внутри не обойтись.

Вот как будет выглядеть твой типичный **build.gradle** файл:

**build.gradle**
```groovy
plugins {
    id 'java'
}

group = 'org.pulsar'
version = '0.0.1-SNAPSHOT'

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = 21
    targetCompatibility = 21
}

test {
    useJUnitPlatform()
}
```

Да, это всё отлично работает, но 99% разработчиков вообще не вдупляют, как это вообще работает.

Нужно понимать, что в **build.gradle** мы пишем код на языке **Groovy**, который полностью совместим с **Java**, а это значит, что можно писать абсолютно любой код. А вот такой лаконичный код позволяет писать конструкция языка **Groovy** под названием **Closure**.

Вот как выглядели бы код без использования **DSL**:

**build.gradle**
```groovy
Project project = getProject()

project.getPlugins().apply("java")

project.setGroup("org.pulsar")
project.setVersion("0.0.1-SNAPSHOT")

project.getRepositories().mavenCentral()

project.getDependencies().implementation("org.springframework.boot:spring-boot-starter:4.1.0")

project.java {
    setSourceCompatibility(21)
    setTargetCompatibility(21)
}

project.test.useJUnitPlatform()
```

Здесь прикол в том, что почти все вызовы так или иначе делегируются на объект типа **Project**. Для каждого **build.gradle** файла создаётся объект типа **Project**. Для каждого файла **settings.gradle** создаётся объект типа **Settings** и все вызовы делегируются ему.


# 2026-07-01 | source/target compatibility
- - -
Gradle позволяет указывать параметры **source/target** compatibility для **javac**.

**build.gradle**
```groovy
java {
    sourceCompatibility = 25
    targetCompatibility = 25
}
```

- **sourceCompatibility** - устанавливает флаг **-source** для утилиты **javac**. Данный параметр указывает, какую версию языка разрешено использовать компилятору. Если ты установишь `sourceCompatibility = 8` и попытаешься использовать фичи из более поздних версий, то компилятор выдаст ошибку.

```java
public class Test {
    
    public record MyRecord() {}
}
```

```bash
javac -source 8 Test.java # Выдаст ошибку
```

- **targetCompatibility** - устанавливает флаг **-target** для утилиты **javac**. Данный параметр указывает версию **JVM**, под которую нужно сгенерировать `.class` файлы.

```bash
javac -target 11 Test.java
```


# 2026-07-01 | How to specify the Java version in Gradle
- - -
В Gradle есть 2 способа указать компилятору версию **Java**, в которую нужно собирать исходный код:

- **source/target compatibility** - стандартный способ. Недостаток данного способа в том, что **Gradle** ищет версию **Java** на текущем компьютере. Если её нет, то будет собирать под ту версию, которая есть. В итоге ты указал версию 25, собралось под 21 и у тебя всё работает, а у остальных нет

```groovy
java {
    sourceCompatibility = 25
    targetCompatibility = 25
}
```

- **toolchain** - улучшенная версия прошлого способа. Для указания версии использует флаг **--release** для **javac**, который не даёт возможности возникнуть ошибкам, когда ты компилируешь в java 8, но используешь фичи из java 21, в итоге в рантайме у тебя будет ошибка.

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
```

Кроме того, вместе с версией **Java** можно указать, чтобы собирались архивы с исходным кодом и javadoc:

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    
    withSourcesJar()
    withJavadocJar()
}
```




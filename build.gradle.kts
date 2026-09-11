import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    alias(libs.plugins.spring.boot) apply false
//    alias(libs.plugins.spring.dependency.management) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

var springBootDependencies = libs.spring.boot.dependencies

subprojects {
    apply(plugin = "java")

    dependencies {
        implementation(platform(springBootDependencies))
//        implementation(platform("org.springframework.boot:spring-boot-dependencies:4.1.0"))
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.withType<Jar> {
    enabled = true
}
//tasks.matching { it.name == "bootJar" }.configureEach {
//    enabled = false;
//}
//
//tasks.matching { it.name == "jar" }.configureEach {
//    enabled = true
//}

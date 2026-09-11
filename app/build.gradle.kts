plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":auth"))
    implementation(project(":user"))
    implementation(project(":chat"))
    implementation(project(":message"))
    implementation(project(":infrastructure"))
}

dependencies {
    implementation(libs.spring.boot.starter.liquibase)
}

dependencies {
    runtimeOnly(libs.postgresql.driver)
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}

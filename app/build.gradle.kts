import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.spring.boot)
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

tasks.withType<BootJar> {
    enabled = true
}

tasks.withType<Jar> {
    enabled = false
}

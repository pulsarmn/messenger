plugins {
    java
//    alias(libs.plugins.spring.boot)
//    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
}

//dependencies {
//    implementation(libs.spring.boot.starter.web)
//    implementation(libs.spring.boot.starter.test)
//    implementation(libs.spring.boot.starter.data.jpa)
//    implementation(libs.spring.boot.starter.security)
//    implementation(libs.spring.boot.starter.websocket)
//    implementation(libs.spring.boot.starter.liquibase)
//    implementation(libs.spring.boot.starter.validation)
//}
//
//dependencies {
//    runtimeOnly(libs.postgresql.driver)
//    implementation(libs.bouncy.castle)
//    implementation(libs.nimbus.jose.jwt)
//}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.jar {
    enabled = false
}

dependencies {
    implementation(project(":infrastructure"))
}

dependencies {
    implementation(libs.spring.security.core)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
}

dependencies {
    runtimeOnly(libs.postgresql.driver)
}

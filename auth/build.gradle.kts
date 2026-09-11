dependencies {
    implementation(project(":user"))
    implementation(project(":infrastructure"))
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
}

dependencies {
    runtimeOnly(libs.postgresql.driver)
    implementation(libs.nimbus.jose.jwt)
}

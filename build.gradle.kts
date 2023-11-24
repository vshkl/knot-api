val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val hikaricp_version: String by project
val dotenv_version: String by project
val detekt_version: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
    id("io.gitlab.arturbosch.detekt") version ("1.23.3")
}

group = "com.knot"
version = "0.0.1"

application {
    mainClass.set("com.knot.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

detekt {
    toolVersion = detekt_version
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

repositories {
    mavenCentral()
}

dependencies {
    /* Server */
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-resources:$ktor_version")
    /* Serialization */
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    /* Database */
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    /* Env */
    implementation("io.github.cdimascio:dotenv-kotlin:$dotenv_version")
    /* Logging */
    implementation("ch.qos.logback:logback-classic:$logback_version")
    /* Detekt */
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")
    /* Testing */
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.6")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
}

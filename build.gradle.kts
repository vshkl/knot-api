val arrowVersion: String by project
val detektVersion: String by project
val dotenvVersion: String by project
val exposedVersion: String by project
val hikaricpVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val postgresVersion: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
}

group = "com.knot"
version = "0.0.1"

application {
    mainClass.set("com.knot.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

detekt {
    toolVersion = detektVersion
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
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-resources:$ktorVersion")
    /* Serialization */
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    /* Database */
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("com.zaxxer:HikariCP:$hikaricpVersion")
    /* Env */
    implementation("io.github.cdimascio:dotenv-kotlin:$dotenvVersion")
    /* Logging */
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    /* ArrowKt */
    implementation(platform("io.arrow-kt:arrow-stack:$arrowVersion"))
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-fx-coroutines")
    /* Detekt */
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    /* Testing */
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.6")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
}

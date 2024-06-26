plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "com.github.lamba92"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://jitpack.io")
}

application {
    mainClass = "com.github.lamba92.MainKt"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.6.3")
    implementation("io.ktor:ktor-client-cio:2.3.10")
    implementation("io.ktor:ktor-server-cio:2.3.10")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.10")
    implementation("com.github.Dansoftowner:jSystemThemeDetector:3.6")
    implementation("ch.qos.logback:logback-classic:1.5.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")
    implementation("io.ktor:ktor-client-logging:2.3.10")
    implementation("org.jetbrains.compose.desktop:desktop-jvm-windows-x64:1.6.2")
    implementation("org.jetbrains.compose.material3:material3:1.6.2")
    implementation("io.ktor:ktor-server-call-logging:2.3.10")
    implementation("io.ktor:ktor-server-rate-limit:2.3.10")
    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    startScripts {
        doLast {
            windowsScript.writeText(windowsScript.readText().replace("set CLASSPATH=.*", "set CLASSPATH=.;%APP_HOME%/lib/*"))
        }
    }
}
kotlin {
    jvmToolchain(17)
}
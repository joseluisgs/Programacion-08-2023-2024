plugins {
    kotlin("jvm") version "1.9.23"
}

group = "dev.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Logger
    implementation("org.lighthousegames:logging:1.3.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    // H2
    implementation("com.h2database:h2:2.2.224")
    // Para cargar scrips de la base de datos
    implementation("org.mybatis:mybatis:3.5.13")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.10")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
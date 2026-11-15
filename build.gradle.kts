plugins {
    kotlin("jvm") version "2.3.21"
    application
    jacoco
}

group = "ie.setu"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.register("verifyProjectStructure") {
    group = "verification"
    description = "Checks that the application skeleton and automated tests are present."
    doLast {
        val required = listOf(
            "README.md",
            "src/main/kotlin/Main.kt",
            "src/main/kotlin/model/Member.kt",
            "src/main/kotlin/model/ClassSession.kt",
            "src/main/kotlin/model/Booking.kt",
            "src/main/kotlin/service/GymService.kt",
            "src/test/kotlin/service/GymServiceTest.kt"
        )
        val missing = required.filterNot { file(it).isFile }
        check(missing.isEmpty()) { "Required project files missing: ${missing.joinToString()}" }
    }
}

tasks.named("check") {
    dependsOn("verifyProjectStructure")
}

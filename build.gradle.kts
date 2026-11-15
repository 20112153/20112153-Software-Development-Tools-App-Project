plugins {
    kotlin("jvm") version "2.3.21"
    application
    jacoco
}

group = "ie.setu"
version = "0.4.0-SNAPSHOT"

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
            "src/test/kotlin/service/GymServiceTest.kt",
            "src/main/kotlin/ui/GymConsole.kt",
            "src/main/kotlin/persistence/GymFileStore.kt",
            "src/main/kotlin/service/GymSnapshot.kt",
            "src/test/kotlin/persistence/GymFileStoreTest.kt"
        )
        val missing = required.filterNot { file(it).isFile }
        check(missing.isEmpty()) { "Required project files missing: ${missing.joinToString()}" }
    }
}

tasks.register("verifyDocumentation") {
    group = "verification"
    description = "Checks that the README covers building, testing and required workflow."
    doLast {
        val guide = file("README.md").readText().lowercase()
        listOf("gradle", "testing", "workflow", "json").forEach { term ->
            check(term in guide) { "README is missing the topic: $term" }
        }
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = "0.65".toBigDecimal()
            }
        }
    }
}

tasks.named("check") {
    dependsOn("verifyProjectStructure", "verifyDocumentation", "jacocoTestCoverageVerification")
}

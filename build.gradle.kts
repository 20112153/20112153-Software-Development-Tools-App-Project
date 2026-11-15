import java.io.ByteArrayInputStream

plugins {
    kotlin("jvm") version "2.3.21"
    application
    jacoco
}

group = "ie.setu"
version = "1.0.0"

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
                counter = "INSTRUCTION"
                minimum = "0.90".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

/**
 * Runs the REAL console entry point with scripted input in a disposable build directory.
 * Confirms that startup, menu input, bookings and JSON autosave work together.
 */
tasks.register<JavaExec>("verifyConsoleSmoke") {
    group = "verification"
    description = "Exercises the console entry point and checks the saved JSON data."
    dependsOn(tasks.named("classes"))
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("MainKt")
    workingDir = layout.buildDirectory.dir("console-smoke").get().asFile
    standardInput = ByteArrayInputStream(
        "1\n1\nAlex\n0\n2\n1\nYoga\n2\n0\n3\n1\n1\n1\n0\n0\n".toByteArray()
    )
    doFirst {
        workingDir.mkdirs()
        workingDir.resolve("data/gym-data.json").delete()
    }
    doLast {
        val saveFile = workingDir.resolve("data/gym-data.json")
        check(saveFile.isFile) { "Console did not save a JSON file." }
        val json = saveFile.readText()
        listOf("\"members\"", "\"classes\"", "\"bookings\"", "Alex", "Yoga").forEach { value ->
            check(value in json) { "Console smoke test did not save expected value: $value" }
        }
        println("Console smoke test passed and JSON was written.")
    }
}

tasks.named("check") {
    dependsOn(
        "verifyProjectStructure", "verifyDocumentation",
        "verifyConsoleSmoke", "jacocoTestCoverageVerification"
    )
}

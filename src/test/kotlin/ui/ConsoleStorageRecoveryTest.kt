package ui

import org.junit.jupiter.api.io.TempDir
import persistence.GymFileStore
import service.GymService
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConsoleStorageRecoveryTest {
    @TempDir lateinit var directory: Path

    private fun console(
        file: Path,
        gym: GymService,
        answers: List<String>,
        beforeFirstInput: (() -> Unit)? = null
    ): String {
        val iterator = answers.iterator()
        val output = mutableListOf<String>()
        var firstInput = true
        GymConsole(
            gym = gym,
            input = {
                if (firstInput) {
                    firstInput = false
                    beforeFirstInput?.invoke()
                }
                if (iterator.hasNext()) iterator.next() else null
            },
            output = output::add,
            dataFile = file
        ).run()
        return output.joinToString("\n")
    }

    @Test
    fun `successful manual load restores the saved version`() {
        val file = directory.resolve("gym.json")
        val original = GymService()
        original.addMember("Alex")
        GymFileStore().save(file, original)
        val live = GymService()
        val output = console(
            file, live,
            listOf("1", "1", "Sam", "0", "5", "1", "2", "0", "0")
        )
        assertTrue(output.contains("Member #2 added."))
        assertTrue(output.contains("Data loaded."))
        assertEquals(listOf("Alex"), live.listMembers().map { it.name })
        assertEquals(original.snapshot(), live.snapshot())
    }

    @Test
    fun `failed manual load protects corrupt file even after more edits`() {
        val file = directory.resolve("gym.json")
        val old = GymService()
        old.addMember("Alex")
        GymFileStore().save(file, old)
        val live = GymService()
        val output = console(
            file, live,
            listOf("5", "1", "1", "Sam", "0", "0"),
            beforeFirstInput = { Files.writeString(file, "{broken") }
        )
        assertTrue(output.contains("Autosave disabled"))
        assertEquals("{broken", Files.readString(file))
        assertEquals(listOf("Alex", "Sam"), live.listMembers().map { it.name })
    }

    @Test
    fun `explicit Save allows recovering a corrupt manual load`() {
        val file = directory.resolve("gym.json")
        val old = GymService()
        old.addMember("Alex")
        GymFileStore().save(file, old)
        val live = GymService()
        val output = console(
            file, live,
            listOf("5", "1", "1", "Sam", "0", "4", "0"),
            beforeFirstInput = { Files.writeString(file, "{broken") }
        )
        assertTrue(output.contains("Autosave disabled"))
        assertTrue(output.contains("Data saved."))
        val recovered = GymService()
        GymFileStore().load(file, recovered)
        assertEquals(listOf("Alex", "Sam"), recovered.listMembers().map { it.name })
    }
}

package ui

import service.GymService
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PersistenceConsoleTest {
    @TempDir lateinit var dir: Path

    private fun run(file: Path, gym: GymService, vararg answers: String): String {
        val replies = answers.iterator()
        val lines = mutableListOf<String>()
        GymConsole(
            gym = gym,
            input = { if (replies.hasNext()) replies.next() else null },
            output = lines::add,
            dataFile = file
        ).run()
        return lines.joinToString("\n")
    }

    @Test fun `exit autosaves and restart autoloads`() {
        val path = dir.resolve("save").resolve("gym.json")
        val first = GymService()
        val result = run(path, first, "1", "1", "Alex", "0", "2", "1", "Yoga", "1", "0", "3", "1", "1", "1", "0", "0")
        assertTrue(result.contains("Data saved."))
        val second = GymService()
        val messages = run(path, second, "3", "2", "0", "0")
        assertTrue(messages.contains("Saved data loaded."))
        assertTrue(messages.contains("Member #1 -> Class #1"))
        assertEquals(1, second.bookingsForMember(1).size)
    }

    @Test fun `malformed startup data is not overwritten`() {
        val file = dir.resolve("bad.json")
        Files.writeString(file, "{broken")
        val before = Files.readString(file)
        val message = run(file, GymService(), "0")
        assertTrue(message.contains("Could not load saved data"))
        assertEquals(before, Files.readString(file))
    }
}

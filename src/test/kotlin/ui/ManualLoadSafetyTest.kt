package ui

import persistence.GymFileStore
import service.GymService
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ManualLoadSafetyTest {
    @TempDir lateinit var dir: Path

    @Test fun `failed manual load does not overwrite the damaged file on exit`() {
        val path = dir.resolve("gym.json")
        val gym = GymService()
        gym.addMember("Existing")
        GymFileStore().save(path, gym)
        val inputs = listOf("5", "0").iterator()
        var changed = false
        val lines = mutableListOf<String>()
        GymConsole(
            gym = GymService(),
            input = {
                if (!changed) {
                    Files.writeString(path, "{bad json")
                    changed = true
                }
                if (inputs.hasNext()) inputs.next() else null
            },
            output = lines::add,
            dataFile = path
        ).run()
        assertEquals("{bad json", Files.readString(path))
        assertTrue(lines.any { it.contains("Autosave disabled") })
    }
}

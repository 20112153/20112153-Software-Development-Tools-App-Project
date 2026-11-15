package ui

import org.junit.jupiter.api.io.TempDir
import service.GymService
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FullWorkflowTest {
    @TempDir lateinit var folder: Path

    private fun run(file: Path, service: GymService, answers: List<String>): String {
        val responses = answers.iterator()
        val output = mutableListOf<String>()
        GymConsole(
            gym = service,
            input = { if (responses.hasNext()) responses.next() else null },
            output = output::add,
            dataFile = file
        ).run()
        return output.joinToString("\n")
    }

    @Test fun `members classes bookings move reports and restart work together`() {
        val file = folder.resolve("gym.json")
        val first = GymService()
        val session = listOf(
            "1",                      // Members menu
            "1", "Alex",             // Add first member
            "1", "Sam",              // Add second member
            "0",                      // Main
            "2",                      // Classes menu
            "1", "Yoga", "1",       // Yoga has one place
            "1", "Spin", "2",       // Spin has two places
            "0",                      // Main
            "3",                      // Bookings menu
            "1", "1", "1",          // Alex books Yoga
            "1", "2", "1",          // Sam cannot overbook Yoga
            "1", "2", "2",          // Sam books Spin
            "4", "1", "1", "2",   // Move Alex from Yoga to Spin
            "0",                      // Main
            "6",                      // Search and reports
            "4", "5",                // Class attendance and member counts
            "0", "0"                 // Exit: autosave
        )
        val initialOutput = run(file, first, session)
        assertTrue(initialOutput.contains("Class is full."))
        assertTrue(initialOutput.contains("Booking moved to the new class."))
        assertTrue(initialOutput.contains("#1: Yoga - 0/1 booked, 1 free"))
        assertTrue(initialOutput.contains("#2: Spin - 2/2 booked, 0 free"))
        assertEquals(2, first.bookingsForClass(2).size)
        assertTrue(Files.exists(file))

        val second = GymService()
        val restoredOutput = run(file, second, listOf("3", "2", "0", "6", "4", "0", "0"))
        assertTrue(restoredOutput.contains("Saved data loaded."))
        assertTrue(restoredOutput.contains("Member #1 -> Class #2 (Spin)"))
        assertTrue(restoredOutput.contains("Member #2 -> Class #2 (Spin)"))
        assertEquals(first.snapshot(), second.snapshot())
        assertEquals(3, second.addMember("Third").id)
    }
}

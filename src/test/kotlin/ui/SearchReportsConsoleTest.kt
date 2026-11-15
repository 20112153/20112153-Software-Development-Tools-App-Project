package ui

import service.GymService
import kotlin.test.Test
import kotlin.test.assertTrue

class SearchReportsConsoleTest {
    @Test fun `search and reports menu prints relevant results`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 2)
        gym.bookClass(member.id, session.id)
        val inputs = listOf("6", "1", "al", "2", "yOg", "3", "4", "5", "0", "0").iterator()
        val output = mutableListOf<String>()
        GymConsole(gym, { if (inputs.hasNext()) inputs.next() else null }, output::add).run()
        assertTrue(output.any { it.contains("#1: Alex") })
        assertTrue(output.any { it.contains("#1: Yoga") })
        assertTrue(output.any { it.contains("(1 free)") })
        assertTrue(output.any { it.contains("1/2 booked") })
        assertTrue(output.any { it.contains("1 bookings") })
    }

    @Test fun `invalid search and empty data show helpful messages`() {
        val inputs = listOf("6", "1", "  ", "2", "not-there", "3", "4", "5", "0", "0").iterator()
        val output = mutableListOf<String>()
        GymConsole(GymService(), { if (inputs.hasNext()) inputs.next() else null }, output::add).run()
        assertTrue(output.any { it.contains("Enter a search term.") })
        assertTrue(output.any { it.contains("No matching classes.") })
        assertTrue(output.any { it.contains("No classes with available spaces.") })
        assertTrue(output.any { it.contains("No members found.") })
    }
}

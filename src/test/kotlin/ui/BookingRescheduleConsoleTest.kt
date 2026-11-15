package ui

import service.GymService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookingRescheduleConsoleTest {
    @Test fun `console moves existing booking using option four`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val from = gym.addClass("Yoga", 1)
        val to = gym.addClass("Spin", 1)
        gym.bookClass(member.id, from.id)
        val replies = listOf("3", "4", "1", "1", "2", "2", "0", "0").iterator()
        val lines = mutableListOf<String>()
        GymConsole(gym, { if (replies.hasNext()) replies.next() else null }, lines::add).run()
        assertTrue(lines.any { it.contains("Booking moved") })
        assertTrue(lines.any { it.contains("Member #1 -> Class #2") })
        assertTrue(gym.bookingsForClass(from.id).isEmpty())
        assertEquals(1, gym.bookingsForClass(to.id).size)
    }
}

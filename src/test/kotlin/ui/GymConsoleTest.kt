package ui

import service.BookingResult
import service.GymService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GymConsoleTest {
    private fun interact(gym: GymService, vararg answers: String): String {
        val responses = answers.iterator()
        val messages = mutableListOf<String>()
        GymConsole(
            gym = gym,
            input = { if (responses.hasNext()) responses.next() else null },
            output = { messages.add(it) }
        ).run()
        return messages.joinToString("\n")
    }

    @Test
    fun `member menu supports add list update and delete`() {
        val gym = GymService()
        val messages = interact(gym,
            "1", "1", "Alex", "2", "3", "1", "Sam", "2",
            "4", "1", "y", "2", "0", "0"
        )
        assertTrue(messages.contains("Member #1 added."))
        assertTrue(messages.contains("#1: Alex"))
        assertTrue(messages.contains("#1: Sam"))
        assertTrue(messages.contains("Member deleted."))
        assertTrue(messages.contains("No members found."))
        assertTrue(gym.listMembers().isEmpty())
    }

    @Test
    fun `class menu supports add list update and delete`() {
        val gym = GymService()
        val messages = interact(gym,
            "2", "1", "Yoga", "3", "2", "3", "1", "Pilates", "5",
            "2", "4", "1", "y", "2", "0", "0"
        )
        assertTrue(messages.contains("#1: Yoga (0/3 booked)"))
        assertTrue(messages.contains("#1: Pilates (0/5 booked)"))
        assertTrue(messages.contains("Class deleted."))
        assertTrue(gym.listClasses().isEmpty())
    }

    @Test
    fun `booking menu supports create list and cancel`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 2)
        val messages = interact(gym,
            "3", "1", "1", "1", "2", "3", "1", "1", "2", "0", "0"
        )
        assertTrue(messages.contains("Booking created."))
        assertTrue(messages.contains("Member #1 -> Class #1 (Yoga)"))
        assertTrue(messages.contains("Booking cancelled."))
        assertTrue(messages.contains("No bookings found."))
        assertTrue(gym.bookingsForMember(member.id).isEmpty())
        assertEquals(session.id, 1)
    }

    @Test
    fun `invalid inputs and business errors are displayed without crashing`() {
        val gym = GymService()
        gym.addMember("Alex")
        gym.addMember("Sam")
        gym.addClass("Yoga", 1)
        val messages = interact(gym,
            "invalid",
            "1", "1", " ", "3", "not a number", "0",
            "3", "1", "1", "1", "1", "1", "1", "1", "2", "1", "0",
            "0"
        )
        assertTrue(messages.contains("Invalid option."))
        assertTrue(messages.contains("Member name cannot be blank."))
        assertTrue(messages.contains("Please enter a positive whole number."))
        assertTrue(messages.contains("Booking created."))
        assertTrue(messages.contains("already booked"))
        assertTrue(messages.contains("Class is full."))
    }

    @Test
    fun `member deletion confirmation protects bookings`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 1)
        assertEquals(BookingResult.CREATED, gym.bookClass(member.id, session.id))
        val messages = interact(gym, "1", "4", "1", "n", "0", "0")
        assertTrue(messages.contains("Deletion cancelled."))
        assertEquals(1, gym.bookingsForMember(member.id).size)
        interact(gym, "1", "4", "1", "y", "0", "0")
        assertTrue(gym.bookingsForClass(session.id).isEmpty())
    }

    @Test
    fun `end of input closes console menus safely`() {
        val gym = GymService()
        assertTrue(interact(gym).contains("Gym Class Manager"))
        assertTrue(interact(gym, "1").contains("Members:"))
    }
}

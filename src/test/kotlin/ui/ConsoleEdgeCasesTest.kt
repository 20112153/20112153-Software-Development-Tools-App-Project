package ui

import service.BookingResult
import service.GymService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConsoleEdgeCasesTest {
    private fun play(gym: GymService, vararg replies: String): String {
        val answers = replies.iterator()
        val lines = mutableListOf<String>()
        GymConsole(
            gym = gym,
            input = { if (answers.hasNext()) answers.next() else null },
            output = lines::add
        ).run()
        return lines.joinToString("\n")
    }

    @Test
    fun `non-positive and non-numeric inputs never create records`() {
        val gym = GymService()
        val result = play(
            gym,
            "1", "3", "0", "4", "-9", "0",
            "2", "1", "Yoga", "0", "0",
            "3", "1", "abc", "0", "0"
        )
        assertTrue(result.contains("Please enter a positive whole number."))
        assertTrue(gym.listMembers().isEmpty())
        assertTrue(gym.listClasses().isEmpty())
        assertTrue(gym.snapshot().bookings.isEmpty())
    }

    @Test
    fun `deletion confirmation and missing IDs do not damage existing bookings`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 1)
        assertEquals(BookingResult.CREATED, gym.bookClass(member.id, session.id))
        val result = play(
            gym, "1", "4", "1", "n", "3", "999", "Nobody",
            "4", "999", "y", "0", "2", "4", "1", "n", "0", "0"
        )
        assertTrue(result.contains("Deletion cancelled."))
        assertTrue(result.contains("Member not found."))
        assertEquals("Alex", gym.listMembers().single().name)
        assertEquals("Yoga", gym.listClasses().single().name)
        assertEquals(1, gym.bookingsForClass(session.id).size)
    }

    @Test
    fun `console rejects lowering capacity below current bookings`() {
        val gym = GymService()
        val alice = gym.addMember("Alice")
        val bob = gym.addMember("Bob")
        val session = gym.addClass("Yoga", 2)
        gym.bookClass(alice.id, session.id)
        gym.bookClass(bob.id, session.id)
        val result = play(gym, "2", "3", "1", "Pilates", "1", "2", "0", "0")
        assertTrue(result.contains("Capacity cannot be smaller"))
        assertTrue(result.contains("#1: Yoga (2/2 booked)"))
        assertEquals(2, gym.listClasses().single().capacity)
    }

    @Test
    fun `console move to full class preserves the original booking`() {
        val gym = GymService()
        val alice = gym.addMember("Alice")
        val bob = gym.addMember("Bob")
        val yoga = gym.addClass("Yoga", 1)
        val spin = gym.addClass("Spin", 1)
        gym.bookClass(alice.id, yoga.id)
        gym.bookClass(bob.id, spin.id)
        val result = play(gym, "3", "4", "1", "1", "2", "2", "0", "0")
        assertTrue(result.contains("New class is full. Original booking kept."))
        assertTrue(result.contains("Member #1 -> Class #1 (Yoga)"))
        assertEquals(1, gym.bookingsForClass(yoga.id).size)
        assertEquals(1, gym.bookingsForClass(spin.id).size)
    }

    @Test
    fun `missing bookings and unknown classes produce clear errors`() {
        val gym = GymService()
        gym.addMember("Alex")
        gym.addClass("Yoga", 2)
        val result = play(
            gym, "3", "1", "1", "999", "3", "999", "1",
            "4", "1", "1", "999", "0", "0"
        )
        assertTrue(result.contains("Class not found."))
        assertTrue(result.contains("Booking not found."))
        assertTrue(result.contains("Original booking not found."))
        assertTrue(gym.snapshot().bookings.isEmpty())
    }
}

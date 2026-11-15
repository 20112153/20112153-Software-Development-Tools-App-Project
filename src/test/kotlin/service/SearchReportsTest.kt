package service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SearchReportsTest {
    private val gym = GymService()

    @Test fun `search is case insensitive and trims the term`() {
        gym.addMember("Alice Smith")
        gym.addMember("Bob")
        gym.addMember("Alicia")
        gym.addClass("Morning Yoga", 3)
        gym.addClass("Spin", 2)
        assertEquals(listOf("Alice Smith", "Alicia"), gym.searchMembers(" aLi ").map { it.name })
        assertEquals(listOf("Morning Yoga"), gym.searchClasses("YOGA").map { it.name })
        assertTrue(gym.searchClasses("none").isEmpty())
    }

    @Test fun `blank searches are rejected`() {
        assertFailsWith<IllegalArgumentException> { gym.searchMembers("  ") }
        assertFailsWith<IllegalArgumentException> { gym.searchClasses("") }
    }

    @Test fun `capacity filter and summaries respond to booking changes`() {
        val alex = gym.addMember("Alex")
        val sam = gym.addMember("Sam")
        val yoga = gym.addClass("Yoga", 1)
        val spin = gym.addClass("Spin", 2)
        assertEquals(2, gym.classesWithSpace().size)
        gym.bookClass(alex.id, yoga.id)
        gym.bookClass(sam.id, spin.id)
        assertEquals(listOf(spin.id), gym.classesWithSpace().map { it.classId })
        assertEquals(0, gym.classBookingSummary().first { it.classId == yoga.id }.remaining)
        assertEquals(listOf(1, 1), gym.memberBookingCounts().map { it.count })
        gym.cancelBooking(alex.id, yoga.id)
        assertEquals(1, gym.classBookingSummary().first { it.classId == yoga.id }.remaining)
        assertEquals(2, gym.classesWithSpace().size)
        assertEquals(listOf(0, 1), gym.memberBookingCounts().map { it.count })
    }

    @Test fun `empty reports are safe and deleting class removes its bookings`() {
        assertTrue(gym.classBookingSummary().isEmpty())
        assertTrue(gym.memberBookingCounts().isEmpty())
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 1)
        gym.bookClass(member.id, session.id)
        gym.deleteClass(session.id)
        assertTrue(gym.classBookingSummary().isEmpty())
        assertEquals(0, gym.memberBookingCounts().single().count)
    }
}

package service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GymServiceTest {
    private val gym = GymService()

    @Test
    fun `member and class IDs start at one and do not get reused`() {
        val first = gym.addMember("Alex")
        assertEquals(1, first.id)
        assertTrue(gym.deleteMember(first.id))
        assertEquals(2, gym.addMember("Sam").id)
        assertEquals(1, gym.addClass("Yoga", 4).id)
    }

    @Test
    fun `invalid names and capacities are rejected`() {
        assertFailsWith<IllegalArgumentException> { gym.addMember("  ") }
        assertFailsWith<IllegalArgumentException> { gym.addClass("", 2) }
        assertFailsWith<IllegalArgumentException> { gym.addClass("Yoga", 0) }
    }

    @Test
    fun `members and classes can be updated and deleted`() {
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 3)
        assertTrue(gym.updateMember(member.id, " Sam "))
        assertTrue(gym.updateClass(session.id, "Pilates", 5))
        assertEquals("Sam", gym.listMembers().single().name)
        assertEquals("Pilates", gym.listClasses().single().name)
        assertFalse(gym.updateMember(999, "Nobody"))
        assertFalse(gym.deleteClass(999))
        assertTrue(gym.deleteClass(session.id))
        assertTrue(gym.deleteMember(member.id))
    }

    @Test
    fun `many to many bookings support multiple members and classes`() {
        val alex = gym.addMember("Alex")
        val sam = gym.addMember("Sam")
        val yoga = gym.addClass("Yoga", 2)
        val spin = gym.addClass("Spin", 2)
        assertEquals(BookingResult.CREATED, gym.bookClass(alex.id, yoga.id))
        assertEquals(BookingResult.CREATED, gym.bookClass(alex.id, spin.id))
        assertEquals(BookingResult.CREATED, gym.bookClass(sam.id, yoga.id))
        assertEquals(2, gym.bookingsForClass(yoga.id).size)
        assertEquals(2, gym.bookingsForMember(alex.id).size)
    }

    @Test
    fun `duplicate bookings and full classes are rejected`() {
        val alex = gym.addMember("Alex")
        val sam = gym.addMember("Sam")
        val yoga = gym.addClass("Yoga", 1)
        assertEquals(BookingResult.CREATED, gym.bookClass(alex.id, yoga.id))
        assertEquals(BookingResult.ALREADY_BOOKED, gym.bookClass(alex.id, yoga.id))
        assertEquals(BookingResult.CLASS_FULL, gym.bookClass(sam.id, yoga.id))
    }

    @Test
    fun `nonexistent members and classes cannot be booked`() {
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 1)
        assertEquals(BookingResult.MEMBER_NOT_FOUND, gym.bookClass(404, session.id))
        assertEquals(BookingResult.CLASS_NOT_FOUND, gym.bookClass(member.id, 404))
    }

    @Test
    fun `cancelling a booking makes a place available`() {
        val alex = gym.addMember("Alex")
        val sam = gym.addMember("Sam")
        val yoga = gym.addClass("Yoga", 1)
        gym.bookClass(alex.id, yoga.id)
        assertTrue(gym.cancelBooking(alex.id, yoga.id))
        assertFalse(gym.cancelBooking(alex.id, yoga.id))
        assertEquals(BookingResult.CREATED, gym.bookClass(sam.id, yoga.id))
    }

    @Test
    fun `deleting a member or class removes associated bookings`() {
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 3)
        gym.bookClass(member.id, session.id)
        assertTrue(gym.deleteMember(member.id))
        assertTrue(gym.bookingsForClass(session.id).isEmpty())
        val next = gym.addMember("Sam")
        gym.bookClass(next.id, session.id)
        assertTrue(gym.deleteClass(session.id))
        assertTrue(gym.bookingsForMember(next.id).isEmpty())
    }

    @Test
    fun `class capacity cannot be lowered below existing bookings`() {
        val first = gym.addMember("Alex")
        val second = gym.addMember("Sam")
        val session = gym.addClass("Yoga", 3)
        gym.bookClass(first.id, session.id)
        gym.bookClass(second.id, session.id)
        assertFailsWith<IllegalArgumentException> { gym.updateClass(session.id, "Yoga", 1) }
        assertEquals(3, gym.listClasses().single().capacity)
    }
}

package service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookingRescheduleTest {
    private val gym = GymService()

    @Test fun `moving booking changes only its destination`() {
        val member = gym.addMember("Alex")
        val from = gym.addClass("Yoga", 2)
        val to = gym.addClass("Spin", 2)
        assertEquals(BookingResult.CREATED, gym.bookClass(member.id, from.id))
        assertEquals(RescheduleResult.MOVED, gym.rescheduleBooking(member.id, from.id, to.id))
        assertTrue(gym.bookingsForClass(from.id).isEmpty())
        assertEquals(listOf(member.id), gym.bookingsForClass(to.id).map { it.memberId })
    }

    @Test fun `moving to same class is a no-op`() {
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 1)
        gym.bookClass(member.id, session.id)
        assertEquals(RescheduleResult.UNCHANGED, gym.rescheduleBooking(member.id, session.id, session.id))
        assertEquals(1, gym.bookingsForMember(member.id).size)
    }

    @Test fun `invalid destination never deletes existing booking`() {
        val alex = gym.addMember("Alex")
        val sam = gym.addMember("Sam")
        val from = gym.addClass("Yoga", 3)
        val full = gym.addClass("Full", 1)
        val duplicate = gym.addClass("Already Booked", 2)
        gym.bookClass(alex.id, from.id)
        gym.bookClass(sam.id, full.id)
        gym.bookClass(alex.id, duplicate.id)
        assertEquals(RescheduleResult.CLASS_FULL, gym.rescheduleBooking(alex.id, from.id, full.id))
        assertEquals(RescheduleResult.ALREADY_BOOKED, gym.rescheduleBooking(alex.id, from.id, duplicate.id))
        assertEquals(RescheduleResult.CLASS_NOT_FOUND, gym.rescheduleBooking(alex.id, from.id, 999))
        assertEquals(1, gym.bookingsForClass(from.id).size)
    }

    @Test fun `missing original booking and missing member are handled`() {
        val member = gym.addMember("Alex")
        val from = gym.addClass("Yoga", 2)
        val to = gym.addClass("Spin", 2)
        assertEquals(RescheduleResult.BOOKING_NOT_FOUND, gym.rescheduleBooking(member.id, from.id, to.id))
        assertEquals(RescheduleResult.MEMBER_NOT_FOUND, gym.rescheduleBooking(999, from.id, to.id))
        assertTrue(gym.bookingsForClass(to.id).isEmpty())
    }
}

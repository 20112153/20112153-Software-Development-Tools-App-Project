package service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class QualityRegressionTest {
    @Test fun `saving after a booking move retains only its new class`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val old = gym.addClass("Old", 1)
        val new = gym.addClass("New", 1)
        gym.bookClass(member.id, old.id)
        assertEquals(RescheduleResult.MOVED, gym.rescheduleBooking(member.id, old.id, new.id))
        val loaded = GymService()
        loaded.restore(gym.snapshot())
        assertTrue(loaded.bookingsForClass(old.id).isEmpty())
        assertEquals(1, loaded.bookingsForClass(new.id).size)
        assertEquals(0, loaded.classBookingSummary().first { it.classId == old.id }.booked)
    }

    @Test fun `invalid capacity update does not change a class`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 1)
        gym.bookClass(member.id, session.id)
        assertFailsWith<IllegalArgumentException> { gym.updateClass(session.id, "Renamed", 0) }
        assertEquals("Yoga", gym.listClasses().single().name)
        assertEquals(1, gym.listClasses().single().capacity)
    }

    @Test fun `ID overflow is handled instead of producing negative IDs`() {
        val gym = GymService()
        gym.restore(GymSnapshot(emptyList(), emptyList(), emptyList(), Int.MAX_VALUE, Int.MAX_VALUE))
        assertFailsWith<IllegalArgumentException> { gym.addMember("Alex") }
        assertFailsWith<IllegalArgumentException> { gym.addClass("Yoga", 1) }
        assertTrue(gym.listMembers().isEmpty())
        assertTrue(gym.listClasses().isEmpty())
    }
}

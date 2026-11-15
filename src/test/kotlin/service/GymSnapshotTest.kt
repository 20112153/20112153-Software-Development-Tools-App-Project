package service

import model.Booking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GymSnapshotTest {
    @Test fun `snapshot round trip retains bookings and counters`() {
        val gym = GymService()
        val removed = gym.addMember("Removed")
        val saved = gym.addMember("Alex")
        gym.deleteMember(removed.id)
        val oldClass = gym.addClass("Removed", 2)
        val session = gym.addClass("Spin", 1)
        gym.deleteClass(oldClass.id)
        gym.bookClass(saved.id, session.id)
        val loaded = GymService()
        loaded.restore(gym.snapshot())
        assertEquals(1, loaded.bookingsForClass(session.id).size)
        assertEquals(3, loaded.addMember("Sam").id)
        assertEquals(3, loaded.addClass("Yoga", 1).id)
    }

    @Test fun `deleted highest IDs are not reused after restoring`() {
        val gym = GymService()
        val member = gym.addMember("Alex")
        val session = gym.addClass("Yoga", 1)
        gym.deleteMember(member.id)
        gym.deleteClass(session.id)
        val restored = GymService()
        restored.restore(gym.snapshot())
        assertEquals(2, restored.addMember("Sam").id)
        assertEquals(2, restored.addClass("Spin", 1).id)
    }

    @Test fun `invalid snapshot cannot partially replace live data`() {
        val gym = GymService()
        val member = gym.addMember("Valid")
        val session = gym.addClass("Yoga", 1)
        gym.bookClass(member.id, session.id)
        val original = gym.snapshot()
        val candidates = listOf(
            original.copy(bookings = listOf(Booking(999, session.id))),
            original.copy(bookings = listOf(Booking(member.id, session.id), Booking(member.id, session.id))),
            original.copy(nextMemberId = 1),
            original.copy(members = original.members + member),
            original.copy(classes = original.classes.map { it.copy(capacity = 0) }),
            original.copy(formatVersion = 999)
        )
        candidates.forEach { bad ->
            assertFailsWith<IllegalArgumentException> { gym.restore(bad) }
            assertEquals(original, gym.snapshot())
        }
        assertTrue(gym.bookingsForMember(member.id).isNotEmpty())
    }
}

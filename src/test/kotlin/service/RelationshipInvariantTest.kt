package service

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Reproducible stress test of relationship invariants, not a probabilistic pass/fail test. */
class RelationshipInvariantTest {
    @Test
    fun `mixed edits cannot leave dangling overbooked or duplicated relationships`() {
        val random = Random(20261115)
        val gym = GymService()

        repeat(500) { step ->
            val members = gym.listMembers()
            val classes = gym.listClasses()
            when (random.nextInt(10)) {
                0, 1 -> gym.addMember("Member-$step")
                2 -> gym.addClass("Class-$step", random.nextInt(1, 5))
                3 -> if (members.isNotEmpty()) gym.deleteMember(members.random(random).id)
                4 -> if (classes.isNotEmpty()) gym.deleteClass(classes.random(random).id)
                5, 6 -> if (members.isNotEmpty() && classes.isNotEmpty()) {
                    gym.bookClass(members.random(random).id, classes.random(random).id)
                }
                7 -> if (members.isNotEmpty() && classes.isNotEmpty()) {
                    gym.cancelBooking(members.random(random).id, classes.random(random).id)
                }
                8 -> {
                    val bookings = gym.snapshot().bookings
                    if (bookings.isNotEmpty() && classes.isNotEmpty()) {
                        val chosen = bookings.random(random)
                        gym.rescheduleBooking(chosen.memberId, chosen.classId, classes.random(random).id)
                    }
                }
                else -> if (classes.isNotEmpty()) {
                    val chosen = classes.random(random)
                    runCatching { gym.updateClass(chosen.id, "Renamed-$step", random.nextInt(1, 5)) }
                }
            }

            val state = gym.snapshot()
            val memberIds = state.members.map { it.id }.toSet()
            val classById = state.classes.associateBy { it.id }
            assertEquals(state.members.size, memberIds.size)
            assertEquals(state.classes.size, classById.size)
            assertEquals(state.bookings.size, state.bookings.toSet().size)
            assertTrue(state.bookings.all { it.memberId in memberIds && it.classId in classById })
            assertTrue(state.bookings.groupingBy { it.classId }.eachCount()
                .all { (id, count) -> count <= classById.getValue(id).capacity })
            assertTrue(state.nextMemberId > (memberIds.maxOrNull() ?: 0))
            assertTrue(state.nextClassId > (classById.keys.maxOrNull() ?: 0))
            val restored = GymService()
            restored.restore(state)
            assertEquals(state, restored.snapshot())
        }
    }
}

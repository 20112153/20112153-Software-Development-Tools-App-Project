package persistence

import model.Booking
import service.GymService
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class GymFileStoreTest {
    @TempDir lateinit var dir: Path
    private val store = GymFileStore()

    @Test fun `JSON round trip restores links and does not reuse deleted IDs`() {
        val original = GymService()
        val removed = original.addMember("Removed")
        val member = original.addMember("Alex")
        original.deleteMember(removed.id)
        val discarded = original.addClass("Old", 1)
        val session = original.addClass("Yoga", 2)
        original.deleteClass(discarded.id)
        original.bookClass(member.id, session.id)
        val file = dir.resolve("gym.json")
        store.save(file, original)
        val text = Files.readString(file)
        assertTrue(text.contains("\"members\""))
        assertTrue(text.contains("\"bookings\""))
        val loaded = GymService()
        store.load(file, loaded)
        assertEquals(original.snapshot(), loaded.snapshot())
        assertEquals(3, loaded.addMember("New").id)
        assertEquals(3, loaded.addClass("Spin", 1).id)
    }

    @Test fun `saving and loading empty data works`() {
        val file = dir.resolve("nested").resolve("gym.json")
        store.save(file, GymService())
        val loaded = GymService()
        store.load(file, loaded)
        assertEquals(1, loaded.addMember("Sam").id)
    }

    @Test fun `bad or missing files do not change current data`() {
        val gym = GymService()
        gym.addMember("Keep")
        val before = gym.snapshot()
        val file = dir.resolve("bad.json")
        assertFails { store.load(file, gym) }
        assertEquals(before, gym.snapshot())
        Files.writeString(file, "{not valid")
        assertFails { store.load(file, gym) }
        assertEquals(before, gym.snapshot())
        Files.writeString(file, "[]")
        assertFails { store.load(file, gym) }
        assertEquals(before, gym.snapshot())
    }

    @Test fun `invalid references and overbooked snapshots are rejected`() {
        val gym = GymService()
        gym.addMember("Keep")
        val before = gym.snapshot()
        val file = dir.resolve("bad.json")
        val invalidLinks = """{"members":[{"id":1,"name":"Alex"}],"classes":[{"id":1,"name":"Yoga","capacity":1}],"bookings":[{"memberId":404,"classId":1}],"nextMemberId":2,"nextClassId":2,"formatVersion":1}"""
        Files.writeString(file, invalidLinks)
        assertFails { store.load(file, gym) }
        assertEquals(before, gym.snapshot())
        val overCapacity = """{"members":[{"id":1,"name":"Alex"},{"id":2,"name":"Sam"}],"classes":[{"id":1,"name":"Yoga","capacity":1}],"bookings":[{"memberId":1,"classId":1},{"memberId":2,"classId":1}],"nextMemberId":3,"nextClassId":2,"formatVersion":1}"""
        Files.writeString(file, overCapacity)
        assertFails { store.load(file, gym) }
        assertEquals(before, gym.snapshot())
    }
}

package persistence

import org.junit.jupiter.api.io.TempDir
import service.GymService
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class InvalidJsonRegressionTest {
    @TempDir lateinit var folder: Path

    @Test fun `invalid JSON types IDs links and versions cannot replace valid state`() {
        val file = folder.resolve("gym.json")
        val service = GymService()
        service.addMember("Original member")
        val before = service.snapshot()
        val valid = """{"members":[],"classes":[],"bookings":[],"nextMemberId":1,"nextClassId":1,"formatVersion":1}"""
        val invalidFiles = listOf(
            "null",
            "[]",
            "{}",
            valid.replace("\"members\":[]", "\"members\":null"),
            valid.replace("\"members\":[]", "\"members\":{}"),
            valid.replace("\"nextClassId\":1", "\"nextClassId\":\"two\""),
            valid.replace("\"formatVersion\":1", "\"formatVersion\":3"),
            valid.replace("\"nextMemberId\":1", "\"nextMemberId\":0"),
            valid.replace("\"nextMemberId\":1", "\"nextMemberId\":2147483648"),
            valid.replace("\"bookings\":[]", "\"bookings\":[{\"memberId\":8,\"classId\":2}]"),
            valid.replace("\"members\":[]", "\"members\":[{\"id\":1,\"name\":null}]"),
            valid.replace("\"members\":[]", "\"members\":[{\"id\":1,\"name\":\"A\"},{\"id\":1,\"name\":\"B\"}]")
                .replace("\"nextMemberId\":1", "\"nextMemberId\":2")
        )
        for (bad in invalidFiles) {
            Files.writeString(file, bad)
            assertFails { GymFileStore().load(file, service) }
            assertEquals(before, service.snapshot(), "Failed import must leave the old state unchanged")
        }
    }
}

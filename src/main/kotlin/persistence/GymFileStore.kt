package persistence

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import model.Booking
import model.ClassSession
import model.Member
import service.GymService
import service.GymSnapshot
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption.ATOMIC_MOVE
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

/** Saves and loads a complete gym snapshot as readable JSON. */
class GymFileStore {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun save(path: Path, gym: GymService) {
        val destination = path.toAbsolutePath()
        Files.createDirectories(destination.parent)
        val temporary = Files.createTempFile(destination.parent, "gym-", ".tmp")
        try {
            Files.writeString(temporary, gson.toJson(gym.snapshot()))
            try {
                Files.move(temporary, destination, ATOMIC_MOVE, REPLACE_EXISTING)
            } catch (_: AtomicMoveNotSupportedException) {
                Files.move(temporary, destination, REPLACE_EXISTING)
            }
        } finally {
            Files.deleteIfExists(temporary)
        }
    }

    fun load(path: Path, gym: GymService) {
        val element = JsonParser.parseString(Files.readString(path))
        require(element.isJsonObject) { "JSON must contain an object." }
        val root = element.asJsonObject
        val snapshot = GymSnapshot(
            members = array(root, "members").map { item ->
                val obj = objectValue(item)
                Member(integer(obj, "id"), string(obj, "name"))
            },
            classes = array(root, "classes").map { item ->
                val obj = objectValue(item)
                ClassSession(integer(obj, "id"), string(obj, "name"), integer(obj, "capacity"))
            },
            bookings = array(root, "bookings").map { item ->
                val obj = objectValue(item)
                Booking(integer(obj, "memberId"), integer(obj, "classId"))
            },
            nextMemberId = integer(root, "nextMemberId"),
            nextClassId = integer(root, "nextClassId"),
            formatVersion = integer(root, "formatVersion")
        )
        gym.restore(snapshot) // Validation happens before anything in the service is cleared.
    }

    private fun objectValue(item: com.google.gson.JsonElement): JsonObject {
        require(item.isJsonObject) { "Expected a JSON object." }
        return item.asJsonObject
    }

    private fun array(root: JsonObject, field: String): JsonArray {
        val item = root.get(field)
        require(item != null && item.isJsonArray) { "Missing or invalid $field list." }
        return item.asJsonArray
    }

    private fun integer(root: JsonObject, field: String): Int {
        val item = root.get(field)
        require(item != null && item.isJsonPrimitive && item.asJsonPrimitive.isNumber) {
            "Missing or invalid number: $field."
        }
        return item.asString.toIntOrNull()
            ?: throw IllegalArgumentException("Expected whole number: $field.")
    }

    private fun string(root: JsonObject, field: String): String {
        val item = root.get(field)
        require(item != null && item.isJsonPrimitive && item.asJsonPrimitive.isString) {
            "Missing or invalid text: $field."
        }
        return item.asString
    }
}

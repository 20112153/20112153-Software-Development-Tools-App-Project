import service.GymService
import utils.readInt
import utils.readText

/** Thin console interface: business rules live in GymService. */
fun main() {
    val gym = GymService()
    println("Gym Class Manager - starter framework")
    while (true) {
        println("\n1. Add member  2. Add class  3. Book class  4. List data  0. Exit")
        val option = readText("Select an option: ") ?: break
        when (option.toIntOrNull()) {
            1 -> {
                val name = readText("Member name: ") ?: break
                runCatching { gym.addMember(name) }
                    .onSuccess { println("Added member #${it.id}: ${it.name}") }
                    .onFailure { println(it.message) }
            }
            2 -> {
                val name = readText("Class name: ") ?: break
                val capacity = readInt("Capacity: ")
                if (capacity == null) {
                    println("Enter a valid integer for capacity.")
                } else {
                    runCatching { gym.addClass(name, capacity) }
                        .onSuccess { println("Added class #${it.id}: ${it.name}") }
                        .onFailure { println(it.message) }
                }
            }
            3 -> {
                val memberId = readInt("Member ID: ")
                val classId = readInt("Class ID: ")
                if (memberId == null || classId == null) println("IDs must be integers.")
                else println("Booking: ${gym.bookClass(memberId, classId)}")
            }
            4 -> {
                println("Members: ${gym.listMembers()}")
                println("Classes: ${gym.listClasses()}")
                gym.listClasses().forEach { println("Bookings for ${it.name}: ${gym.bookingsForClass(it.id)}") }
            }
            0 -> return
            else -> println("Invalid option.")
        }
    }
    println("Input closed. Goodbye.")
}

package ui

import service.BookingResult
import service.GymService
import utils.readText

/**
 * Simple console menus. All booking rules and data changes are handled by GymService.
 * Input and output can be replaced in tests without reading from the real console.
 */
class GymConsole(
    private val gym: GymService = GymService(),
    private val input: (String) -> String? = ::readText,
    private val output: (String) -> Unit = ::println
) {
    fun run() {
        output("Gym Class Manager")
        while (true) {
            output("")
            output("1. Members  2. Classes  3. Bookings  0. Exit")
            when (ask("Select: ") ?: return) {
                "1" -> membersMenu()
                "2" -> classesMenu()
                "3" -> bookingsMenu()
                "0" -> {
                    output("Goodbye!")
                    return
                }
                else -> output("Invalid option. Please try again.")
            }
        }
    }

    private fun membersMenu() {
        while (true) {
            output("")
            output("Members: 1. Add  2. List  3. Update  4. Delete  0. Back")
            when (ask("Select: ") ?: return) {
                "1" -> addMember()
                "2" -> listMembers()
                "3" -> updateMember()
                "4" -> deleteMember()
                "0" -> return
                else -> output("Invalid option. Please try again.")
            }
        }
    }

    private fun classesMenu() {
        while (true) {
            output("")
            output("Classes: 1. Add  2. List  3. Update  4. Delete  0. Back")
            when (ask("Select: ") ?: return) {
                "1" -> addClass()
                "2" -> listClasses()
                "3" -> updateClass()
                "4" -> deleteClass()
                "0" -> return
                else -> output("Invalid option. Please try again.")
            }
        }
    }

    private fun bookingsMenu() {
        while (true) {
            output("")
            output("Bookings: 1. Book class  2. List bookings  3. Cancel booking  0. Back")
            when (ask("Select: ") ?: return) {
                "1" -> createBooking()
                "2" -> listBookings()
                "3" -> cancelBooking()
                "0" -> return
                else -> output("Invalid option. Please try again.")
            }
        }
    }

    private fun addMember() {
        val name = ask("Member name: ") ?: return
        safely {
            val member = gym.addMember(name)
            output("Member #${member.id} added.")
        }
    }

    private fun listMembers() {
        val members = gym.listMembers()
        if (members.isEmpty()) output("No members found.")
        else members.forEach { output("#${it.id}: ${it.name}") }
    }

    private fun updateMember() {
        val id = positiveInt("Member ID: ") ?: return
        val name = ask("New name: ") ?: return
        safely {
            if (gym.updateMember(id, name)) output("Member updated.")
            else output("Member not found.")
        }
    }

    private fun deleteMember() {
        val id = positiveInt("Member ID: ") ?: return
        if (ask("Delete member and their bookings? (y/N): ")?.lowercase() != "y") {
            output("Deletion cancelled.")
            return
        }
        if (gym.deleteMember(id)) output("Member deleted.")
        else output("Member not found.")
    }

    private fun addClass() {
        val name = ask("Class name: ") ?: return
        val capacity = positiveInt("Capacity: ") ?: return
        safely {
            val session = gym.addClass(name, capacity)
            output("Class #${session.id} added.")
        }
    }

    private fun listClasses() {
        val classes = gym.listClasses()
        if (classes.isEmpty()) output("No classes found.")
        else classes.forEach { session ->
            val booked = gym.bookingsForClass(session.id).size
            output("#${session.id}: ${session.name} (${booked}/${session.capacity} booked)")
        }
    }

    private fun updateClass() {
        val id = positiveInt("Class ID: ") ?: return
        val name = ask("New class name: ") ?: return
        val capacity = positiveInt("New capacity: ") ?: return
        safely {
            if (gym.updateClass(id, name, capacity)) output("Class updated.")
            else output("Class not found.")
        }
    }

    private fun deleteClass() {
        val id = positiveInt("Class ID: ") ?: return
        if (ask("Delete class and its bookings? (y/N): ")?.lowercase() != "y") {
            output("Deletion cancelled.")
            return
        }
        if (gym.deleteClass(id)) output("Class deleted.")
        else output("Class not found.")
    }

    private fun createBooking() {
        val memberId = positiveInt("Member ID: ") ?: return
        val classId = positiveInt("Class ID: ") ?: return
        when (gym.bookClass(memberId, classId)) {
            BookingResult.CREATED -> output("Booking created.")
            BookingResult.MEMBER_NOT_FOUND -> output("Member not found.")
            BookingResult.CLASS_NOT_FOUND -> output("Class not found.")
            BookingResult.ALREADY_BOOKED -> output("Member is already booked into this class.")
            BookingResult.CLASS_FULL -> output("Class is full.")
        }
    }

    private fun listBookings() {
        val entries = gym.listClasses().flatMap { session ->
            gym.bookingsForClass(session.id).map { booking ->
                "Member #${booking.memberId} -> Class #${session.id} (${session.name})"
            }
        }
        if (entries.isEmpty()) output("No bookings found.")
        else entries.forEach(output)
    }

    private fun cancelBooking() {
        val memberId = positiveInt("Member ID: ") ?: return
        val classId = positiveInt("Class ID: ") ?: return
        if (gym.cancelBooking(memberId, classId)) output("Booking cancelled.")
        else output("Booking not found.")
    }

    private fun ask(prompt: String): String? = input(prompt)?.trim()

    private fun positiveInt(prompt: String): Int? {
        val text = ask(prompt) ?: return null
        val number = text.toIntOrNull()
        if (number == null || number <= 0) {
            output("Please enter a positive whole number.")
            return null
        }
        return number
    }

    private fun safely(action: () -> Unit) {
        try {
            action()
        } catch (error: IllegalArgumentException) {
            output("Error: ${error.message}")
        }
    }
}

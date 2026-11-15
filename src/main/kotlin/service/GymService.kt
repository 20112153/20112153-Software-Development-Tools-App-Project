package service

import model.Booking
import model.ClassSession
import model.Member

enum class BookingResult { CREATED, MEMBER_NOT_FOUND, CLASS_NOT_FOUND, ALREADY_BOOKED, CLASS_FULL }

/** Stores members, classes and their many-to-many bookings in memory. */
class GymService {
    private val members = linkedMapOf<Int, Member>()
    private val classes = linkedMapOf<Int, ClassSession>()
    private val bookings = linkedSetOf<Booking>()
    private var nextMemberId = 1
    private var nextClassId = 1

    fun addMember(name: String): Member {
        require(name.isNotBlank()) { "Member name cannot be blank." }
        return Member(nextMemberId++, name.trim()).also { members[it.id] = it }
    }

    fun listMembers(): List<Member> = members.values.toList()

    fun updateMember(id: Int, name: String): Boolean {
        require(name.isNotBlank()) { "Member name cannot be blank." }
        val existing = members[id] ?: return false
        members[id] = existing.copy(name = name.trim())
        return true
    }

    fun deleteMember(id: Int): Boolean {
        if (members.remove(id) == null) return false
        bookings.removeAll { it.memberId == id }
        return true
    }

    fun addClass(name: String, capacity: Int): ClassSession {
        require(name.isNotBlank()) { "Class name cannot be blank." }
        require(capacity > 0) { "Capacity must be positive." }
        return ClassSession(nextClassId++, name.trim(), capacity).also { classes[it.id] = it }
    }

    fun listClasses(): List<ClassSession> = classes.values.toList()

    fun updateClass(id: Int, name: String, capacity: Int): Boolean {
        require(name.isNotBlank()) { "Class name cannot be blank." }
        require(capacity > 0) { "Capacity must be positive." }
        val existing = classes[id] ?: return false
        require(bookings.count { it.classId == id } <= capacity) {
            "Capacity cannot be smaller than the number of existing bookings."
        }
        classes[id] = existing.copy(name = name.trim(), capacity = capacity)
        return true
    }

    fun deleteClass(id: Int): Boolean {
        if (classes.remove(id) == null) return false
        bookings.removeAll { it.classId == id }
        return true
    }

    fun bookClass(memberId: Int, classId: Int): BookingResult {
        if (memberId !in members) return BookingResult.MEMBER_NOT_FOUND
        val session = classes[classId] ?: return BookingResult.CLASS_NOT_FOUND
        val booking = Booking(memberId, classId)
        if (booking in bookings) return BookingResult.ALREADY_BOOKED
        if (bookings.count { it.classId == classId } >= session.capacity) return BookingResult.CLASS_FULL
        bookings.add(booking)
        return BookingResult.CREATED
    }

    fun cancelBooking(memberId: Int, classId: Int): Boolean =
        bookings.remove(Booking(memberId, classId))

    fun bookingsForClass(classId: Int): List<Booking> =
        bookings.filter { it.classId == classId }

    fun bookingsForMember(memberId: Int): List<Booking> =
        bookings.filter { it.memberId == memberId }
}

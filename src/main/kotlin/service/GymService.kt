package service

import model.Booking
import model.ClassSession
import model.Member

enum class BookingResult { CREATED, MEMBER_NOT_FOUND, CLASS_NOT_FOUND, ALREADY_BOOKED, CLASS_FULL }

enum class RescheduleResult { MOVED, UNCHANGED, MEMBER_NOT_FOUND, BOOKING_NOT_FOUND, CLASS_NOT_FOUND, ALREADY_BOOKED, CLASS_FULL }

/** Stores members, classes and their many-to-many bookings in memory. */
class GymService {
    private val members = linkedMapOf<Int, Member>()
    private val classes = linkedMapOf<Int, ClassSession>()
    private val bookings = linkedSetOf<Booking>()
    private var nextMemberId = 1
    private var nextClassId = 1

    fun addMember(name: String): Member {
        require(name.isNotBlank()) { "Member name cannot be blank." }
        require(nextMemberId < Int.MAX_VALUE) { "Member ID limit reached." }
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
        require(nextClassId < Int.MAX_VALUE) { "Class ID limit reached." }
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

    /** Moves a booking only after validating the destination; failures leave the source intact. */
    fun rescheduleBooking(memberId: Int, oldClassId: Int, newClassId: Int): RescheduleResult {
        if (memberId !in members) return RescheduleResult.MEMBER_NOT_FOUND
        val original = Booking(memberId, oldClassId)
        if (original !in bookings) return RescheduleResult.BOOKING_NOT_FOUND
        val destination = classes[newClassId] ?: return RescheduleResult.CLASS_NOT_FOUND
        if (oldClassId == newClassId) return RescheduleResult.UNCHANGED
        if (Booking(memberId, newClassId) in bookings) return RescheduleResult.ALREADY_BOOKED
        if (bookings.count { it.classId == newClassId } >= destination.capacity) return RescheduleResult.CLASS_FULL
        bookings.remove(original)
        bookings.add(Booking(memberId, newClassId))
        return RescheduleResult.MOVED
    }

    fun cancelBooking(memberId: Int, classId: Int): Boolean =
        bookings.remove(Booking(memberId, classId))

    fun searchMembers(term: String): List<Member> {
        require(term.isNotBlank()) { "Enter a search term." }
        return members.values.filter { it.name.contains(term.trim(), ignoreCase = true) }
    }

    fun searchClasses(term: String): List<ClassSession> {
        require(term.isNotBlank()) { "Enter a search term." }
        return classes.values.filter { it.name.contains(term.trim(), ignoreCase = true) }
    }

    fun classBookingSummary(): List<ClassBookingSummary> {
        val counts = bookings.groupingBy { it.classId }.eachCount()
        return classes.values.map { ClassBookingSummary(it.id, it.name, counts[it.id] ?: 0, it.capacity) }
    }

    fun classesWithSpace(): List<ClassBookingSummary> = classBookingSummary().filter { it.remaining > 0 }

    fun memberBookingCounts(): List<MemberBookingCount> {
        val counts = bookings.groupingBy { it.memberId }.eachCount()
        return members.values.map { MemberBookingCount(it.id, it.name, counts[it.id] ?: 0) }
    }

    /** A portable copy of all data, including counters after deletions. */
    fun snapshot(): GymSnapshot = GymSnapshot(
        members = listMembers(),
        classes = listClasses(),
        bookings = bookings.toList(),
        nextMemberId = nextMemberId,
        nextClassId = nextClassId
    )

    /** Validate the entire snapshot before making any changes to live data. */
    fun restore(snapshot: GymSnapshot) {
        require(snapshot.formatVersion == 1) { "Unsupported save file version." }
        val memberIds = snapshot.members.map { it.id }
        val classIds = snapshot.classes.map { it.id }
        require(snapshot.members.all { it.id > 0 && it.name.isNotBlank() }) { "Invalid member details." }
        require(snapshot.classes.all { it.id > 0 && it.name.isNotBlank() && it.capacity > 0 }) {
            "Invalid class details."
        }
        require(memberIds.toSet().size == memberIds.size && classIds.toSet().size == classIds.size) {
            "Duplicate member or class ID."
        }
        require(snapshot.nextMemberId > (memberIds.maxOrNull() ?: 0) && snapshot.nextMemberId > 0) {
            "Invalid next member ID."
        }
        require(snapshot.nextClassId > (classIds.maxOrNull() ?: 0) && snapshot.nextClassId > 0) {
            "Invalid next class ID."
        }
        require(snapshot.bookings.toSet().size == snapshot.bookings.size) { "Duplicate booking." }
        require(snapshot.bookings.all { it.memberId in memberIds && it.classId in classIds }) {
            "Booking references missing member or class."
        }
        val classCapacities = snapshot.classes.associate { it.id to it.capacity }
        require(snapshot.bookings.groupingBy { it.classId }.eachCount().all { (id, count) ->
            count <= classCapacities.getValue(id)
        }) { "Saved class is over capacity." }

        members.clear()
        classes.clear()
        bookings.clear()
        snapshot.members.forEach { members[it.id] = it }
        snapshot.classes.forEach { classes[it.id] = it }
        bookings.addAll(snapshot.bookings)
        nextMemberId = snapshot.nextMemberId
        nextClassId = snapshot.nextClassId
    }

    fun bookingsForClass(classId: Int): List<Booking> =
        bookings.filter { it.classId == classId }

    fun bookingsForMember(memberId: Int): List<Booking> =
        bookings.filter { it.memberId == memberId }
}

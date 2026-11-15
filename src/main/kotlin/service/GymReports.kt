package service

/** Derived reporting values (not stored separately from the bookings). */
data class ClassAttendance(
    val classId: Int,
    val name: String,
    val booked: Int,
    val capacity: Int
) {
    val remaining: Int get() = capacity - booked
}

data class MemberBookingCount(val memberId: Int, val name: String, val count: Int)

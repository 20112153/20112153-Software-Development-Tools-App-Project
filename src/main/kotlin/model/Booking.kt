package model

/** The linking entity for the many-to-many Member <-> ClassSession relationship. */
data class Booking(val memberId: Int, val classId: Int)

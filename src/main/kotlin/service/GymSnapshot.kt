package service

import model.Booking
import model.ClassSession
import model.Member

/** Contents of one JSON save file; counters prevent reusing deleted IDs. */
data class GymSnapshot(
    val members: List<Member>,
    val classes: List<ClassSession>,
    val bookings: List<Booking>,
    val nextMemberId: Int,
    val nextClassId: Int,
    val formatVersion: Int = 1
)

package com.example.attendanceappoffline.domain.repository

import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    suspend fun insertAttendance(record: AttendanceRecord)
    suspend fun getAttendanceRecord(studentId: String, date: String): AttendanceRecord?
    suspend fun getAttendanceForDateAndStudent(
        className: String,
        date: String,
        firstName: String,
        lastName: String
    ): AttendanceRecord?

    suspend fun updateAttendance(
        className: String,
        section: String,
        firstName: String,
        lastName: String,
        date: String,
        isPresent: Boolean
    )

    fun getStudentsWithAttendance(
        className: String,
        section: String,
        selectedDate: String
    ): Flow<List<StudentWithAttendance>>

    suspend fun getAttendanceByStudentIdAndDate(studentId: Int, date: String): AttendanceRecord?

    suspend fun getUnsyncedRecords(): List<AttendanceRecord>

    suspend fun markAsSynced(id: Int)

    suspend fun syncAttendanceData()
}

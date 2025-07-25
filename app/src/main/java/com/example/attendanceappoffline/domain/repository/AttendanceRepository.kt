package com.example.attendanceappoffline.domain.repository

import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    suspend fun insertAttendance(record: AttendanceRecord)
    suspend fun getAttendanceRecord(studentId: String, date: String): AttendanceRecord?
    suspend fun getAttendanceForDateAndStudent(
        className: String,
        date: String,
        fullName: String,
        schoolId: String
    ): AttendanceRecord?

    suspend fun updateAttendance(
        className: String,
        fullName: String,
        rollNumber: String,
        date: String,
        status: String
    )

    fun getStudentsWithAttendance(
        className: String,
        selectedDate: String
    ): Flow<List<StudentWithAttendance>>

    suspend fun getAttendanceByStudentIdAndDate(studentId: Int, date: String): AttendanceRecord?

    suspend fun getUnsyncedRecords(): List<AttendanceRecord>

    suspend fun markAsSynced(id: Int)

    suspend fun syncAttendanceData(dto: AttendanceDto)

    suspend fun addAttendanceToDB(dto: AttendanceDto)
}

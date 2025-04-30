package com.example.attendanceappoffline.domain.usecases

import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import kotlinx.coroutines.flow.Flow

class AttendanceUseCases(private val dao: AttendanceDao) {

    suspend fun insertAttendanceRecord(record: AttendanceRecord) {
        dao.insertAttendanceRecord(record)
    }

    suspend fun getAttendanceRecord(studentId: String, date: String): AttendanceRecord? {
        return dao.getAttendanceRecord(studentId, date)
    }

    suspend fun getAttendanceForDateAndStudent(
        className: String,
        date: String,
        firstName: String,
        lastName: String
    ): AttendanceRecord? {
        return dao.getAttendanceForDateAndStudent(className, date, firstName, lastName)
    }

    suspend fun updateAttendance(
        className: String,
        section: String,
        firstName: String,
        lastName: String,
        date: String,
        isPresent: Boolean
    ) {
        dao.updateAttendance(className, section, firstName, lastName, date, isPresent)
    }

    fun getStudentsWithAttendance(
        className: String,
        section: String,
        selectedDate: String
    ): Flow<List<StudentWithAttendance>> {
        return dao.getStudentsWithAttendance(className, section, selectedDate)
    }

    suspend fun getAttendanceByStudentIdAndDate(
        studentId: Int,
        date: String
    ): AttendanceRecord? {
        return dao.getAttendanceByStudentIdAndDate(studentId, date)
    }
}

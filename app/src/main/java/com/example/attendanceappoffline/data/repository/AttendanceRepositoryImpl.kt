package com.example.attendanceappoffline.data.repository


import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.data.source.remote.AttendanceRemoteRepository
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val dao: AttendanceDao,
    private val apiService: AttendanceRemoteRepository
) : AttendanceRepository {

    override suspend fun insertAttendance(record: AttendanceRecord) {
        dao.insertAttendanceRecord(record)
    }

    override suspend fun getAttendanceRecord(studentId: String, date: String): AttendanceRecord? {
        return dao.getAttendanceRecord(studentId, date)
    }

    override suspend fun getAttendanceForDateAndStudent(
        className: String,
        date: String,
        firstName: String,
        lastName: String
    ): AttendanceRecord? {
        return dao.getAttendanceForDateAndStudent(className, date, firstName, lastName)
    }

    override suspend fun updateAttendance(
        className: String,
        section: String,
        firstName: String,
        lastName: String,
        date: String,
        isPresent: Boolean
    ) {
        dao.updateAttendance(className, section, firstName, lastName, date, isPresent)
    }

    override fun getStudentsWithAttendance(
        className: String,
        section: String,
        selectedDate: String
    ): Flow<List<StudentWithAttendance>> {
        return dao.getStudentsWithAttendance(className, section, selectedDate)
    }

    override suspend fun getAttendanceByStudentIdAndDate(studentId: Int, date: String): AttendanceRecord? {
        return dao.getAttendanceByStudentIdAndDate(studentId, date)
    }

    override suspend fun getUnsyncedRecords(): List<AttendanceRecord> {
        return dao.getUnsyncedRecords()
    }

    override suspend fun markAsSynced(id: Int) {
        dao.markAsSynced(id)
    }

    override suspend fun syncAttendanceData() {
        val unsynced = dao.getUnsyncedRecords()
        unsynced.forEach { record ->
            try {
                val dtoList = listOf(record.toDto()) // Convert to expected type
                apiService.syncAttendance(dtoList)   // Send as List<AttendanceDto>
                dao.markAsSynced(record.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun AttendanceRecord.toDto(): AttendanceDto {
        return AttendanceDto(
            id = this.id,
            studentId = this.studentId,
            date = this.date,
            isPresent = this.isPresent
        )
    }

}


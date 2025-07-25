package com.example.attendanceappoffline.data.repository


import android.net.http.HttpException
import android.util.Log
import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.data.source.remote.repository.AttendanceRemoteRepository
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val dao: AttendanceDao,
    private val api: AttendanceRemoteRepository
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
        fullName: String,
        schoolId: String
    ): AttendanceRecord? {
        return dao.getAttendanceForDateAndStudent(className, date,fullName , schoolId)
    }

    override suspend fun updateAttendance(
        className: String,
        fullName: String,
        rollNumber: String,
        date: String,
        status: String
    ) {
        dao.updateAttendance(className, fullName, rollNumber, date, status)
    }

    override fun getStudentsWithAttendance(
        className: String,
        selectedDate: String
    ): Flow<List<StudentWithAttendance>> {
        return dao.getStudentsWithAttendance(className, selectedDate)
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

    override suspend fun addAttendanceToDB(dto: AttendanceDto) {
        Log.d("attendance in attendance impl",dto.toString())
        try {
            api.addAttendanceToDB(dto)
        } catch (e: retrofit2.HttpException) {
            Log.e("API", "HTTP ${e.code()}: ${e.response()?.errorBody()?.string()}")
        } catch (e: Exception) {
            Log.e("API", "Other error: ${e.localizedMessage}")
        }


//        api.addAttendanceToDB(dto = dto)
    }

    override suspend fun syncAttendanceData(dto: AttendanceDto) {
        val unsynced = dao.getUnsyncedRecords()
//        unsynced.forEach { record ->
//            try {
////                val dtoList = listOf(record.toDto()) // Convert to expected type
//////                apiService.syncAttendance(dtoList)   // Send as List<AttendanceDto>
////                dao.markAsSynced(record.id)
//                api.syncAttendance()
//                dao.markAsSynced(record.id)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
        api.syncAttendance(dto)
    }

//    fun AttendanceRecord.toDto(): AttendanceDto {
//        return AttendanceDto(
//            id = this.id,
//            studentId = this.studentId,
//            date = this.date,
//            isPresent = this.isPresent
//        )
//    }

}


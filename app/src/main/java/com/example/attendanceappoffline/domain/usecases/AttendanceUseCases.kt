package com.example.attendanceappoffline.domain.usecases

import android.util.Log
import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceUseCases @Inject constructor(private val attendanceRepository: AttendanceRepository) {

    suspend fun insertAttendanceRecord(record: AttendanceRecord) {
//        dao.insertAttendanceRecord(record)
        attendanceRepository.insertAttendance(record)
//        attendanceRepository.addAttendanceToDB( AttendanceDto(studenHash = record.studentHash,date = record.date, status = record.status, schoolId = record.schoolId,className = record.className,isSynced = true))
    }

    suspend fun getAttendanceRecord(studentId: String, date: String): AttendanceRecord? {
//        return dao.getAttendanceRecord(studentId, date)
        return  attendanceRepository.getAttendanceRecord(studentId,date)
    }

    suspend fun getAttendanceForDateAndStudent(
        className: String,
        date: String,
        fullName: String,
        schoolId: String
    ): AttendanceRecord? {
        return attendanceRepository.getAttendanceForDateAndStudent(className = className, date = date,fullName=fullName ,schoolId = schoolId)
    }

    suspend fun updateAttendance(
        className: String,
        fullName: String,
        rollNumber: String,
        date: String,
        status: String
    ) {
//        dao.updateAttendance(className, section, firstName, lastName, date, isPresent)
        attendanceRepository.updateAttendance(className,fullName,rollNumber,date, status)
    }

    fun getStudentsWithAttendance(
        className: String,
        selectedDate: String
    ): Flow<List<StudentWithAttendance>> {
        Log.d("mkmkmkmk","sf")
//        return dao.getStudentsWithAttendance(className, section, selectedDate)
        val aa=attendanceRepository.getStudentsWithAttendance(className,selectedDate)
//        Log.d("aa",aa)
//        return  attendanceRepository.getStudentsWithAttendance(className,selectedDate)
        return aa
    }

    suspend fun getAttendanceByStudentIdAndDate(
        studentId: Int,
        date: String
    ): AttendanceRecord? {
//        return dao.getAttendanceByStudentIdAndDate(studentId, date)
        return attendanceRepository.getAttendanceByStudentIdAndDate(studentId,date)
    }

    suspend fun sync(dto: AttendanceDto) {
        val output=attendanceRepository.syncAttendanceData(dto)
        Log.d("apiOutput",output.toString())
//        return output
    }

    suspend fun addAttendanceToDB(dto: AttendanceDto) {
        Log.d("attendance in useccase",dto.toString())
        attendanceRepository.addAttendanceToDB(dto = dto)
    }
}

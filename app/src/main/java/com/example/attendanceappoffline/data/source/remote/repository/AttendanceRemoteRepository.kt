package com.example.attendanceappoffline.data.source.remote.repository

import android.util.Log
import com.example.attendanceappoffline.data.source.remote.api.AttendanceApi
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import javax.inject.Inject

// AttendanceRemoteRepository.kt
class AttendanceRemoteRepository @Inject constructor(private val api: AttendanceApi) {

    suspend fun syncAttendance(dto: AttendanceDto) {
         api.syncAttendance(dto)
    }

    suspend fun addAttendanceToDB(dto: AttendanceDto) {
        Log.d("attendance in remote repo",dto.toString())
        api.addAttendanceToDB(dto)
    }
}

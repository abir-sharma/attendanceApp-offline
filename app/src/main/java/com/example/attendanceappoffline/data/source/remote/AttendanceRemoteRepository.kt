package com.example.attendanceappoffline.data.source.remote

import com.example.attendanceappoffline.data.source.remote.api.AttendanceApi
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto

// AttendanceRemoteRepository.kt
class AttendanceRemoteRepository(private val api: AttendanceApi) {
    suspend fun syncAttendance(records: List<AttendanceDto>) {
        api.syncAttendance(records)
    }
}

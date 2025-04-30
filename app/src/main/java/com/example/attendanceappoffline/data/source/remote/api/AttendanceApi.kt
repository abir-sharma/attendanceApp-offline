package com.example.attendanceappoffline.data.source.remote.api

// AttendanceApi.kt
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AttendanceApi {

    @POST("attendance/sync")
    suspend fun syncAttendance(@Body records: List<AttendanceDto>)

}

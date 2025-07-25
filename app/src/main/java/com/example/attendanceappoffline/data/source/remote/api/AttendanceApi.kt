package com.example.attendanceappoffline.data.source.remote.api

// AttendanceApi.kt
import androidx.camera.core.processing.SurfaceProcessorNode.Out
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceApi {

    @POST("v1/face-rec-app-attendance")
    suspend fun syncAttendance(@Body dto: AttendanceDto)

    @POST("v1/face-rec-app-attendance")
    suspend fun addAttendanceToDB(@Body dto: AttendanceDto)

}

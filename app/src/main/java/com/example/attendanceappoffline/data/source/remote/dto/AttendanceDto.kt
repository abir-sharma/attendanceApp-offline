package com.example.attendanceappoffline.data.source.remote.dto

// AttendanceDto.kt
data class AttendanceDto(
    val id: Int,
    val studentId: String,
    val date: String,
    val isPresent: Boolean
)

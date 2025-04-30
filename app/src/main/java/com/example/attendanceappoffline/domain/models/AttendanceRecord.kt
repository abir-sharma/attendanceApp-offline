package com.example.attendanceappoffline.domain.models

import java.util.Date

data class AttendanceRecord(
    val studentId: String,     // Reference to Student's ID
    val date: String,          // Date in "YYYY-MM-DD" format
    var isPresent: Boolean
)
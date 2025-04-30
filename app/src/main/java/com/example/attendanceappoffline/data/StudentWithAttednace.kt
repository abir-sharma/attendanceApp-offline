package com.example.attendanceappoffline.data

import androidx.room.Embedded
import com.example.attendanceappoffline.data.source.local.entity.StudentEntity

data class StudentWithAttendance(
    @Embedded val student: StudentEntity,
    val isPresent: Boolean? = false // null means no record, so assume false
)

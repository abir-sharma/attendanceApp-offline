package com.example.attendanceappoffline.data.source.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val date: String,  // "YYYY-MM-DD"
    var isPresent: Boolean,
    val isSynced: Boolean = false // <-- Add this
)

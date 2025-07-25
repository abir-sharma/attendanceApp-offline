package com.example.attendanceappoffline.data.source.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentHash: String,
    val date: String,  // "YYYY-MM-DD" it will be ans iso string
    var status: String,
    val schoolId:String,
    val className:String,
    val isSynced: Boolean = false // <-- Add this
)

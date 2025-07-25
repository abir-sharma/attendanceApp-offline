package com.example.attendanceappoffline.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "face_embeddings")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentHash: String,  // 🔗 Unique and used in AttendanceRecord
    val fullName:String,
    val className:String, // it will be like this '11-A'
    val rollNumber:String,
    val schoolId:String,
    val embedding: FloatArray,
    val image: ByteArray, // Store profile image as ByteArray
)

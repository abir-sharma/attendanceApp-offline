package com.example.attendanceappoffline.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "face_embeddings")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,  // 🔗 Unique and used in AttendanceRecord
    val firstName: String,
    val lastName:String,
    val className:String,
    val section:String,
    val embedding: FloatArray,
    val image: ByteArray, // Store profile image as ByteArray
)

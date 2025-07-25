package com.example.attendanceappoffline.data.source.remote.dto

// AttendanceDto.kt
data class AttendanceDto(
//    val id: Int,
//    val studentId: String,
//    val date: String,
//    val isPresent: Boolean

//      val userId:Number,
//      val id:Number,
//      val title:String,
//      val completed:Boolean
      val studentHash:String,
      val date:String,
      val status:String,   // "present" / "absent"
      val schoolId:String,
      val className:String,
      val isSynced:Boolean
)

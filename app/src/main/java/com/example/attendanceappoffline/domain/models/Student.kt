package com.example.attendanceappoffline.domain.models

data class Student(
    val id:String ,
    val firstName: String,
    val lastName: String,
    val className: String,
    val section: String,
    val date: String,
    var isPresent: Boolean,
    val imageRes: Int
)

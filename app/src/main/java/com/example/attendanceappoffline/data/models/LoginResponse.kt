package com.example.attendanceappoffline.data.models

data class LoginResponse(
    val data: LoginData
)

data class LoginData(
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val role: String,
    val schoolId: String,
    val userDetails: String
)

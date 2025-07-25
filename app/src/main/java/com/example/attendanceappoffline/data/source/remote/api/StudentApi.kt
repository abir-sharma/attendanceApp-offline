package com.example.attendanceappoffline.data.source.remote.api

import com.example.attendanceappoffline.data.source.remote.dto.StudentDto
import retrofit2.http.Body
import retrofit2.http.POST

interface StudentApi {
    @POST("v1/face-rec-student")
    suspend fun addStudent(@Body dto: StudentDto)
}
package com.example.attendanceappoffline.data.source.remote.api

import com.example.attendanceappoffline.data.models.LoginResponse
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.data.source.remote.dto.LoginDto
import com.example.attendanceappoffline.domain.models.SchoolApiResponse
import com.example.attendanceappoffline.domain.models.SchoolResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {

    @POST("v1/auth/login")
    suspend fun login(@Body dto: LoginDto): Response<LoginResponse>

    @GET("v1/clicker/school/{id}")
    suspend fun getSchoolDetail(
        @Path("id") schoolId: String
    ): Response<SchoolApiResponse>



}
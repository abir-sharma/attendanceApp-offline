package com.example.attendanceappoffline.data.source.remote.repository

import android.util.Log
import com.example.attendanceappoffline.common.Result
import com.example.attendanceappoffline.data.models.LoginResponse
import com.example.attendanceappoffline.data.source.local.dao.SchoolClassDao
import com.example.attendanceappoffline.data.source.local.entity.SchoolClassEntity
import com.example.attendanceappoffline.data.source.remote.api.AttendanceApi
import com.example.attendanceappoffline.data.source.remote.api.AuthApi
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.data.source.remote.dto.LoginDto
import com.example.attendanceappoffline.domain.models.SchoolApiResponse
import com.example.attendanceappoffline.domain.models.SchoolResponse
import retrofit2.Response
import javax.inject.Inject


class AuthRemoteRepository @Inject constructor(private val api: AuthApi,private val schoolClassDao: SchoolClassDao) {
    suspend fun login(email:String,password:String):Response<LoginResponse> {
         return api.login(LoginDto(email,password))
    }

    suspend fun getSchoolDetail(schoolId: String): Response<SchoolApiResponse> {
    Log.i("schoolId", schoolId)
    val response = api.getSchoolDetail(schoolId)

    // Log the response body as a string (only if it's successful)
    if (response.isSuccessful) {
        Log.d("Response Body", response.body().toString()) // Logs the body
    } else {
        Log.d("Response Error", response.errorBody()?.string() ?: "No error body")
    }

    return response
}

    suspend fun saveClassesLocally(classList:List<SchoolClassEntity>) {
        schoolClassDao.insertAll(classList)
    }

    suspend fun getAllClassLocal():List<SchoolClassEntity> {
        return schoolClassDao.getAllClasses()
    }

}
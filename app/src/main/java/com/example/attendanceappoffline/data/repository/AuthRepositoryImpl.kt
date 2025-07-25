package com.example.attendanceappoffline.data.repository

import android.util.Log
import com.example.attendanceappoffline.domain.models.SchoolResponse
import com.example.attendanceappoffline.domain.repository.AuthRepository
import javax.inject.Inject
import com.example.attendanceappoffline.common.Result
import com.example.attendanceappoffline.data.models.LoginResponse
import com.example.attendanceappoffline.data.source.local.entity.SchoolClassEntity
import com.example.attendanceappoffline.data.source.remote.repository.AuthRemoteRepository

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthRemoteRepository
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        Log.d("dto",email + " " + password)
        return try {
            val response = apiService.login(email, password)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
//            } else {
//                Result.Error("Empty response body or unsuccessful response")
//            }
//            if (response.isSuccessful) {
//                Result.Success(true)
//                Result.Success(response.?body())
            }
        else {
                Result.Error("Login failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Exception occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getSchoolDetails(schoolId: String): Result<SchoolResponse> {
        return try {
           val response = apiService.getSchoolDetail(schoolId)
//            if (response.isSuccessful && response.body() != null) {
//                Result.Success(response.body()!!)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body.data)
//                Log.d("corrBody",body.toString())
            } else {
                Result.Error("Failed to fetch school details: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Exception occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun saveClassesLocally(classList:List<SchoolClassEntity>) {
        apiService.saveClassesLocally(classList)
    }
    override  suspend fun getAllClassLocal():List<SchoolClassEntity> {
        return apiService.getAllClassLocal()
    }

}

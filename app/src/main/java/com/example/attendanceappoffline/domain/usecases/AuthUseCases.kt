package com.example.attendanceappoffline.domain.usecases


import com.example.attendanceappoffline.domain.repository.AuthRepository
import com.example.attendanceappoffline.common.Result
import com.example.attendanceappoffline.data.models.LoginResponse
import com.example.attendanceappoffline.data.source.local.entity.SchoolClassEntity
import com.example.attendanceappoffline.domain.models.SchoolResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            if (email.isBlank() || password.isBlank()) {
                Result.Error("Email and password must not be empty")
            } else {
                authRepository.login(email, password)
            }
        }
    }


    suspend fun getSchoolDetails(schoolId: String):Result<SchoolResponse> {
//        if (schoolId.isBlank()) {
//            return@withContext Result.Error("School ID must not be empty")
//        }
//        return@withContext authRepository.getSchoolDetails(schoolId)
        return withContext(Dispatchers.IO) {
            if (schoolId.isBlank()) {
                Result.Error("Not getting school Id")
            } else {
                authRepository.getSchoolDetails(schoolId)
            }
        }
    }

    suspend fun saveClassesLocally(classList:List<SchoolClassEntity>) {
        authRepository.saveClassesLocally(classList)
    }

    suspend fun getAllClassLocal():List<SchoolClassEntity> {
        return authRepository.getAllClassLocal()
    }
}

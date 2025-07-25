// AuthRepository.kt
package com.example.attendanceappoffline.domain.repository

import com.example.attendanceappoffline.common.Result
import com.example.attendanceappoffline.data.models.LoginResponse
import com.example.attendanceappoffline.data.source.local.entity.SchoolClassEntity
import com.example.attendanceappoffline.domain.models.SchoolResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun getSchoolDetails(schoolId: String): Result<SchoolResponse>
    suspend fun saveClassesLocally(classList:List<SchoolClassEntity>)
    suspend fun getAllClassLocal():List<SchoolClassEntity>
}

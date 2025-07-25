package com.example.attendanceappoffline.domain.repository


import com.example.attendanceappoffline.data.source.local.entity.StudentEntity
import com.example.attendanceappoffline.data.source.remote.dto.StudentDto

interface StudentsRepository {
    suspend fun insertEmbedding(embedding: StudentEntity)

    suspend fun getAllEmbeddings(className: String,schoolId: String): List<StudentEntity>

    suspend fun saveStudentToDB(dto: StudentDto)
    suspend fun getAllFaceEmbeddings(): List<StudentEntity>

    suspend fun getStudentIdByDetails(
        className: String,
        fullName: String,
        schoolId:String
    ): String
}

package com.example.attendanceappoffline.domain.repository


import com.example.attendanceappoffline.data.source.local.entity.StudentEntity

interface StudentsRepository {
    suspend fun insertEmbedding(embedding: StudentEntity)

    suspend fun getAllEmbeddings(className: String, section: String): List<StudentEntity>

    suspend fun getAllFaceEmbeddings(): List<StudentEntity>

    suspend fun getStudentIdByDetails(
        className: String,
        section: String,
        firstName: String,
        lastName: String
    ): String
}

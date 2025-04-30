package com.example.attendanceappoffline.domain.usecases


import com.example.attendanceappoffline.data.source.local.entity.StudentEntity
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao

class StudentUseCases(private val dao: StudentsDao) {

    suspend fun insertEmbedding(embedding: StudentEntity) {
        dao.insertEmbedding(embedding)
    }

    suspend fun getAllEmbeddings(className: String, section: String): List<StudentEntity> {
        return dao.getAllEmbeddings(className, section)
    }

    suspend fun getAllFaceEmbeddings(): List<StudentEntity> {
        return dao.getAllFaceEmbeddings()
    }

    suspend fun getStudentIdByDetails(
        className: String,
        section: String,
        firstName: String,
        lastName: String
    ): String {
        return dao.getStudentIdByDetails(className, section, firstName, lastName)
    }
}

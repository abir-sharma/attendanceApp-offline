package com.example.attendanceappoffline.data.repository

import com.example.attendanceappoffline.data.source.local.entity.StudentEntity
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
import com.example.attendanceappoffline.domain.repository.StudentsRepository

class StudentsRepositoryImpl(
    private val dao: StudentsDao
) : StudentsRepository {

    override suspend fun insertEmbedding(embedding: StudentEntity) {
        dao.insertEmbedding(embedding)
    }

    override suspend fun getAllEmbeddings(className: String, section: String): List<StudentEntity> {
        return dao.getAllEmbeddings(className, section)
    }

    override suspend fun getAllFaceEmbeddings(): List<StudentEntity> {
        return dao.getAllFaceEmbeddings()
    }

    override suspend fun getStudentIdByDetails(
        className: String,
        section: String,
        firstName: String,
        lastName: String
    ): String {
        return dao.getStudentIdByDetails(className, section, firstName, lastName)
    }
}

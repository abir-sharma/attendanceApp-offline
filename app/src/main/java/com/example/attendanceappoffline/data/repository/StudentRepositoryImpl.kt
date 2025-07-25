package com.example.attendanceappoffline.data.repository

import android.util.Log
import com.example.attendanceappoffline.data.source.local.entity.StudentEntity
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
import com.example.attendanceappoffline.data.source.remote.dto.StudentDto
import com.example.attendanceappoffline.data.source.remote.repository.StudentRemoteRepository
import com.example.attendanceappoffline.domain.repository.StudentsRepository
import javax.inject.Inject

class StudentsRepositoryImpl @Inject constructor(
    private val dao: StudentsDao,
    private val studentApi:StudentRemoteRepository
) : StudentsRepository {

    override suspend fun insertEmbedding(embedding: StudentEntity) {
        dao.insertEmbedding(embedding)
    }

    override suspend fun getAllEmbeddings(className: String,schoolId: String): List<StudentEntity> {
        return dao.getAllEmbeddings(className,schoolId)
    }

    override suspend fun getAllFaceEmbeddings(): List<StudentEntity> {
        return dao.getAllFaceEmbeddings()
    }

    override suspend fun saveStudentToDB(dto: StudentDto) {
        Log.d("student in impl",dto.toString())
        studentApi.addStudent(dto)
    }
    override suspend fun getStudentIdByDetails(
        className: String,
        fullName: String,
        schoolId:String
    ): String {
        Log.d("class in imp",className)
        Log.d("full name in imp",fullName)
        Log.d("rollNumber in im",schoolId)
        return dao.getStudentIdByDetails(className = className, fullName =  fullName,schoolId = schoolId)
    }
}

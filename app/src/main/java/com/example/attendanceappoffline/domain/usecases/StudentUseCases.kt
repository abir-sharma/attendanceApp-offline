package com.example.attendanceappoffline.domain.usecases


import android.util.Log
import com.example.attendanceappoffline.data.source.local.entity.StudentEntity
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
import com.example.attendanceappoffline.data.source.remote.dto.StudentDto
import com.example.attendanceappoffline.domain.repository.StudentsRepository
import javax.inject.Inject

class StudentUseCases @Inject constructor(private val studentsRepository: StudentsRepository) {

    suspend fun insertEmbedding(embedding: StudentEntity) {
//        dao.insertEmbedding(embedding)
        studentsRepository.insertEmbedding(embedding)
    }

    suspend fun getAllEmbeddings(className: String,schoolId: String): List<StudentEntity> {
        return  studentsRepository.getAllEmbeddings(className,schoolId)
    }

    suspend fun getAllFaceEmbeddings(): List<StudentEntity> {
        return  studentsRepository.getAllFaceEmbeddings()
    }

    suspend fun saveStudentToDB(dto: StudentDto) {
        Log.d("student in usecase",dto.toString())
        studentsRepository.saveStudentToDB(dto)
    }

    suspend fun getStudentIdByDetails(
        className: String,
        fullName: String,
        schoolId:String
    ): String {
        Log.d("className in use case",className)
        Log.d("rollNumber in use case",schoolId)
        Log.d("fullname in use case",fullName)
        return  studentsRepository.getStudentIdByDetails(className = className, fullName = fullName,schoolId = schoolId)
    }
}

package com.example.attendanceappoffline.data.source.remote.repository

import android.util.Log
import com.example.attendanceappoffline.data.source.remote.api.AttendanceApi
import com.example.attendanceappoffline.data.source.remote.api.StudentApi
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.data.source.remote.dto.StudentDto
import javax.inject.Inject

class StudentRemoteRepository @Inject constructor(private  val api: StudentApi){
    suspend fun addStudent(dto:StudentDto) {
        Log.d("student in remote repo",dto.toString())
        api.addStudent(dto)
    }
}

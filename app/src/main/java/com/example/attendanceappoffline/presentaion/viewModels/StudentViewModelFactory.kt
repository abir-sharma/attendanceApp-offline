package com.example.attendanceappoffline.presentaion.viewModels

import StudentViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao

class StudentViewModelFactory(private val studentsDao: StudentsDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(studentsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
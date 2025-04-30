package com.example.attendanceappoffline.presentaion.viewModels

import AttendanceViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.attendanceappoffline.data.attendance.AttendanceDao

class AttendanceViewModelFactory(private val attendanceDao: AttendanceDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AttendanceViewModel(attendanceDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
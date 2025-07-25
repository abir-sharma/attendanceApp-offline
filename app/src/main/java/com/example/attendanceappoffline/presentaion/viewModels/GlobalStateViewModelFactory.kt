//package com.example.attendanceappoffline.presentaion.viewModels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
//import com.example.attendanceappoffline.data.attendance.AttendanceDao
//
//class GlobalStateViewModelFactory(private val studentsDao: StudentsDao, private val attendanceDao: AttendanceDao) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(GlobalStateViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return GlobalStateViewModel(studentsDao,attendanceDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

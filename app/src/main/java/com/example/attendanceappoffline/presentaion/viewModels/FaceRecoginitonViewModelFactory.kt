//package com.example.attendanceappoffline.presentaion.viewModels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
//import com.example.attendanceappoffline.data.attendance.AttendanceDao
//
//class FaceRecognitionViewModelFactory(private val faceDao: StudentsDao, private val attendanceDao: AttendanceDao) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(FaceRecognitionViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return FaceRecognitionViewModel(faceDao,attendanceDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

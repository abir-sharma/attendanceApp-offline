package com.example.attendanceappoffline.navigation

import AttendanceViewModel
import StudentViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendanceappoffline.common.Routes
import com.example.attendanceappoffline.data.source.local.MyDatabase
import com.example.attendanceappoffline.presentaion.ui.Home
import com.example.attendanceappoffline.presentaion.viewModels.AttendanceViewModelFactory
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModelFactory
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModelFactory
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModelFactory

@Composable
fun MyAppNavigation(navController: NavHostController) {
    val context= LocalContext.current
    val db = remember { MyDatabase.getDatabase(context) }
    val studentDao = remember { db.studentDao() }
    val attendanceDao = remember { db.attendanceDao()}

    val studentViewModel: StudentViewModel = viewModel(
        factory = StudentViewModelFactory(studentDao)
    )
    val attendanceViewModel: AttendanceViewModel = viewModel(
        factory = AttendanceViewModelFactory(attendanceDao)
    )
    val globalStateViewModel:GlobalStateViewModel= viewModel(
        factory = GlobalStateViewModelFactory(studentDao,attendanceDao)
    )
    val faceRecognitionViewModel:FaceRecognitionViewModel= viewModel(
        factory = FaceRecognitionViewModelFactory(studentDao,attendanceDao)
    )



    NavHost(navController = navController, startDestination = Routes.HomePage) {

        // Home Screen
        composable(Routes.HomePage) {
            Home(studentViewModel, attendanceViewModel,globalStateViewModel, faceRecognitionViewModel)
        }
    }
}

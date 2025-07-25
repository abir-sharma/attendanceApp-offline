package com.example.attendanceappoffline.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendanceappoffline.common.LoginPreferenceManager
import com.example.attendanceappoffline.common.Routes
import com.example.attendanceappoffline.data.source.local.MyDatabase
import com.example.attendanceappoffline.presentaion.ui.Home
import com.example.attendanceappoffline.presentaion.viewModels.AttendanceViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
//import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModelFactory
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModel


@Composable
fun MyAppNavigation(navController: NavHostController) {
    val studentViewModel: StudentViewModel = hiltViewModel()
    val attendanceViewModel: AttendanceViewModel = hiltViewModel()

    val globalStateViewModel:GlobalStateViewModel= hiltViewModel()
    val faceRecognitionViewModel:FaceRecognitionViewModel= hiltViewModel()
    val authViewModel:AuthViewModel= hiltViewModel()

    val context = LocalContext.current.applicationContext
    val loginPrefs = remember { LoginPreferenceManager(context) }
    val isLoggedIn by loginPrefs.isLoggedIn.collectAsState(initial = false)

//    LaunchedEffect(isLoggedIn) {
//        if (isLoggedIn) {
//            navController.navigate("home") {
//                popUpTo("login") { inclusive = true }
//            }
//        }
//    }



    NavHost(navController = navController, startDestination = if (isLoggedIn) Routes.HomePage else Routes.LoginPage) {
        composable(Routes.LoginPage) {
            LoginScreen(navController,authViewModel)
        }

        // Home Screen
        composable(Routes.HomePage) {
            Home(studentViewModel, attendanceViewModel,globalStateViewModel, faceRecognitionViewModel,authViewModel)
        }
    }
}

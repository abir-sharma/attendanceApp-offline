package com.example.attendanceappoffline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.attendanceappoffline.common.Routes
import com.example.attendanceappoffline.navigation.MyAppNavigation
import com.example.attendanceappoffline.presentaion.ui.Home
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.ui.theme.AttendanceAppOfflineTheme
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            AttendanceAppOfflineTheme {
                val navController = rememberNavController()
                MyAppNavigation(navController)
            }
        }
    }

}


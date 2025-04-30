package com.example.attendanceappoffline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.attendanceappoffline.navigation.MyAppNavigation
import com.example.attendanceappoffline.ui.theme.AttendanceAppOfflineTheme

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


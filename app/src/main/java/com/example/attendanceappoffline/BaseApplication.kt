package com.example.attendanceappoffline

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.example.attendanceappoffline.domain.usecases.AttendanceUseCases
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        registerReceiver(
            NetworkChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }
}


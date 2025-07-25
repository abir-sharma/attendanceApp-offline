package com.example.attendanceappoffline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.di.UseCaseEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                UseCaseEntryPoint::class.java
            )
            CoroutineScope(Dispatchers.IO).launch {
//                hiltEntryPoint.attendanceUseCases().sync()
            }
        }
    }
}

package com.example.attendanceappoffline.data.source.remote

// RetrofitInstance.kt
import com.example.attendanceappoffline.data.source.remote.api.AttendanceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: AttendanceApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://your-api-base-url.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AttendanceApi::class.java)
    }
}

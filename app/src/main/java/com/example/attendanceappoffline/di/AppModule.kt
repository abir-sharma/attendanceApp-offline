// AppModule.kt
package com.example.attendanceappoffline.di // match your actual package name

import android.app.Application
import androidx.room.Room
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.data.repository.AttendanceRepositoryImpl
import com.example.attendanceappoffline.data.source.local.MyDatabase
import com.example.attendanceappoffline.data.source.remote.AttendanceRemoteRepository
import com.example.attendanceappoffline.data.source.remote.api.AttendanceApi
import com.example.attendanceappoffline.domain.repository.AttendanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MyDatabase {
        return Room.databaseBuilder(
            app,
            MyDatabase::class.java,
            "attendance_db"
        ).build()
    }

    @Provides
    fun provideAttendanceDao(db: MyDatabase): AttendanceDao = db.attendanceDao()

    @Provides
    @Singleton
    fun provideApiService(): AttendanceApi {
        return Retrofit.Builder()
            .baseUrl("") // change to your actual URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AttendanceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttendanceRepository(
        dao: AttendanceDao,
        apiService: AttendanceRemoteRepository
    ): AttendanceRepository {
        return AttendanceRepositoryImpl(dao, apiService)
    }
}

// vAppModule.kt
package com.example.attendanceappoffline.di // match your actual package name

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.attendanceappoffline.common.LoginPreferenceManager
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.data.repository.AttendanceRepositoryImpl
import com.example.attendanceappoffline.data.repository.AuthRepositoryImpl
import com.example.attendanceappoffline.data.repository.StudentsRepositoryImpl
import com.example.attendanceappoffline.data.source.local.MyDatabase
import com.example.attendanceappoffline.data.source.local.dao.SchoolClassDao
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
import com.example.attendanceappoffline.data.source.remote.repository.AttendanceRemoteRepository
import com.example.attendanceappoffline.data.source.remote.api.AttendanceApi
import com.example.attendanceappoffline.data.source.remote.api.AuthApi
import com.example.attendanceappoffline.data.source.remote.api.StudentApi
import com.example.attendanceappoffline.data.source.remote.repository.AuthRemoteRepository
import com.example.attendanceappoffline.data.source.remote.repository.StudentRemoteRepository
import com.example.attendanceappoffline.domain.repository.AttendanceRepository
import com.example.attendanceappoffline.domain.repository.AuthRepository
import com.example.attendanceappoffline.domain.repository.StudentsRepository
import com.example.attendanceappoffline.domain.usecases.AttendanceUseCases
import com.example.attendanceappoffline.domain.usecases.AuthUseCase
import com.example.attendanceappoffline.domain.usecases.StudentUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
//    https://jsonplaceholder.typicode.com/
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pibox-backend.betterpw.live/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAttendanceApi(retrofit: Retrofit): AttendanceApi {
        return retrofit.create(AttendanceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStudentApi(retrofit: Retrofit): StudentApi {
        return retrofit.create(StudentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttendanceDao(db: MyDatabase): AttendanceDao = db.attendanceDao()

    @Provides
    @Singleton
    fun provideSchoolClassDao(db: MyDatabase): SchoolClassDao = db.schoolClassDao()

    @Provides
    @Singleton
    fun provideStudentDao(db: MyDatabase): StudentsDao = db.studentDao()

    @Provides
    @Singleton
    fun provideStudentUseCases(repository: StudentsRepository): StudentUseCases {
        return StudentUseCases(repository)
    }

    @Provides
    @Singleton
    fun provideAttendanceUseCases(attendanceRepository: AttendanceRepository): AttendanceUseCases {
        return AttendanceUseCases(attendanceRepository)
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(authRepository: AuthRepository): AuthUseCase {
        return AuthUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAttendanceRepositoryImpl(attendanceDao: AttendanceDao,api: AttendanceRemoteRepository):AttendanceRepository {
        return AttendanceRepositoryImpl(attendanceDao,api)
    }
//    @Provides
//    @Singleton
//    fun provideAttendanceRepositoryImpl(api: AttendanceApi): AttendanceRepository {
//        return AttendanceRemoteRepository(api)
//    }
    @Provides
    @Singleton
    fun provideStudentRepositoryImpl(studentsDao: StudentsDao,studentRemoteRepository: StudentRemoteRepository):StudentsRepository {
        return StudentsRepositoryImpl(studentsDao,studentRemoteRepository)
    }

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(authRemoteRepository: AuthRemoteRepository):AuthRepository {
        return AuthRepositoryImpl(authRemoteRepository)
    }

    @Provides
    @Singleton
    fun provideLoginPreferenceManager(
        @ApplicationContext context: Context
    ): LoginPreferenceManager {
        return LoginPreferenceManager(context)
    }
//    @Provides
//    @Singleton
//    fun provideAuthRemoteRepository(api:AuthApi,schoolClassDao: SchoolClassDao):AuthRemoteRepository {
//        return AuthRemoteRepository(api,schoolClassDao)
//    }

//    @Provides
//    @Singleton
//    fun provideAttendanceRepository(
//        dao: AttendanceDao,
//        apiService: AttendanceRemoteRepository
//    ): AttendanceRepository {
//        return AttendanceRepositoryImpl(dao, apiService)
//    }
}

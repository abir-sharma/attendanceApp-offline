package com.example.attendanceappoffline.di


import com.example.attendanceappoffline.domain.usecases.AttendanceUseCases
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UseCaseEntryPoint {
    fun attendanceUseCases(): AttendanceUseCases
}

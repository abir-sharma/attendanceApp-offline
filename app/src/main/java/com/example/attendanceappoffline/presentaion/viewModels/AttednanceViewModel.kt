package com.example.attendanceappoffline.presentaion.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.domain.models.AttendanceRecord
import com.example.attendanceappoffline.domain.usecases.AttendanceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceUseCases : AttendanceUseCases
) : ViewModel() {

    var startAttendance by mutableStateOf(false)
    var showToastTakeAttednance by mutableStateOf(false)


    private val _studentsWithAttendance = MutableStateFlow<List<StudentWithAttendance>>(emptyList())
    val studentsWithAttendance: StateFlow<List<StudentWithAttendance>> = _studentsWithAttendance

    var presentCountt by mutableStateOf("0")
    var absentCountt by mutableStateOf("0")
    var percentagee by mutableStateOf("0")





    fun loadStudentsWithAttendance(className: String,date: String) { //        Log.d("debugDataatt",className+" "+date+" "+section)
        Log.d("className",className)
        Log.d("data",date)
        Log.d("className",className)
//        Log.d("section",section)*/
        viewModelScope.launch {
            attendanceUseCases.getStudentsWithAttendance(className,date)
                .collect { studentList ->
                    _studentsWithAttendance.value = studentList
                    calculateAttendanceStats(studentList)
                    Log.d("studentsListatt",className + date+ studentList.toString())

                }
        }
    }

    fun calculateAttendanceStats(students: List<StudentWithAttendance>) {
        Log.d("calculateAttendance", "called")
        val presentCount = students.count { it.isPresent == true }
        val absentCount = students.size - presentCount
        val percentage = if (students.isNotEmpty()) {
            (presentCount.toFloat() / students.size) * 100
        } else {
            0f
        }


        presentCountt = presentCount.toString()
        absentCountt = absentCount.toString()
        percentagee = percentage.toString()

//        Log.d("AttendanceStats", .toString())
    }


    fun openTakeAttendanceToast(value:Boolean) {
        showToastTakeAttednance=value
    }

    fun closeTakeAttendanceToast(value:Boolean) {
        showToastTakeAttednance=value
    }

    fun startStartAttednance(value:Boolean) {
        startAttendance=value
    }
    fun stopStartAttendnace(value:Boolean) {
        startAttendance=value
    }

}

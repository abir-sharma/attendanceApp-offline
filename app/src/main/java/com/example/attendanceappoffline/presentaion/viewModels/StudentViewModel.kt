package com.example.attendanceappoffline.presentaion.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
import com.example.attendanceappoffline.domain.usecases.StudentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

//import com.ml.quaterion.facenetdetection.R
@HiltViewModel
class StudentViewModel @Inject constructor(private val studentUseCases: StudentUseCases) : ViewModel() {
    val calendar = Calendar.getInstance()

    var AddStudentForm by mutableStateOf(false)
    var showToastAddStudent by mutableStateOf(false)

    var selectedClassNameWithSection by  mutableStateOf("Select Class")
    var selectedDate by mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time))



    var studentId by  mutableStateOf("")
    var fullName by  mutableStateOf("")
    var rollNumber by  mutableStateOf("")
    var className by mutableStateOf("")
    var section by mutableStateOf("")
    var schoolId by mutableStateOf("")


    fun updateSelectedDate(date:String) {
        selectedDate=date
    }

    fun updateSelectedClassNameWithSection(uscws:String) {
        selectedClassNameWithSection=uscws
    }
    fun updateStudentId(id: String) {
        studentId = id
    }

    fun updateFullName(name:String) {
        fullName=name
    }

    fun updateRollNumber(name:String) {
        rollNumber=name
    }

    fun updateSection(newSection:String) {
        section=newSection
    }

    fun updateClassName(newClass:String) {
        className=newClass
    }

    fun updateSchoolId(sId:String) {
        schoolId=sId
    }

    fun openToastAddStudent(value: Boolean) {
        showToastAddStudent=value
    }

    fun closeToastAddStudent(value: Boolean) {
        showToastAddStudent=value
    }

    fun openAddStudentForm(value:Boolean) {
        AddStudentForm = value
    }

    fun closeAddStudentForm(value:Boolean) {
        AddStudentForm = value
    }

}

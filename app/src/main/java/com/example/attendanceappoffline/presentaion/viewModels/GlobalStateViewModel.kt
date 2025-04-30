package com.example.attendanceappoffline.presentaion.viewModels

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.source.local.entity.StudentEntity
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class GlobalStateViewModel(private val faceDao: StudentsDao, private val attendanceDao: AttendanceDao): ViewModel() {

//    init {
//        loadFaceEmbeddings()
//    }


    var isRegistering by  mutableStateOf(false)

    var dropdownDate by mutableStateOf("")

    val calendar = java.util.Calendar.getInstance()
    var isCameraAvailable by mutableStateOf(true)

//    private val _faceBounds = MutableStateFlow<List<Rect>>(emptyList())
//    val faceBounds: StateFlow<List<Rect>> = _faceBounds

    private val _studentsWithAttendance = MutableStateFlow<List<StudentWithAttendance>>(emptyList())
    val studentsWithAttendance: StateFlow<List<StudentWithAttendance>> = _studentsWithAttendance

    private val _recognizedName = MutableStateFlow("Unknown")
    val recognizedName: StateFlow<String> = _recognizedName


    private val _faceEmbeddings = MutableStateFlow<List<StudentEntity>>(emptyList())
    val faceEmbeddings: StateFlow<List<StudentEntity>> = _faceEmbeddings

    private val _classList = MutableStateFlow<List<String>>(emptyList())
    val classList: StateFlow<List<String>> = _classList

//    fun updateFaceBounds(faces: List<Rect>) {
//        _faceBounds.value = faces
//    }

    fun updateIsCameraAvailable() {
        if (isCameraAvailable) {
            isCameraAvailable=false
        }
        else {
            isCameraAvailable=true
        }
    }

    fun updateClassListFromDatabase() {
        viewModelScope.launch {
            val students = faceDao.getAllFaceEmbeddings()
            val classNamesWithSections = students
                .map { "${it.className} ${it.section}" } // Combine className and section
                .distinct()
                .sorted()
            _classList.value = classNamesWithSections
        }
    }


    fun addClassNameIfNotExists(newClass: String) {
        viewModelScope.launch {
            val currentList = _classList.value.toList() // create a safe copy
            if (newClass.isNotBlank() && newClass !in currentList) {
                val updated = currentList.toMutableList().apply { add(newClass) }
                _classList.value = updated.sorted()
            }
            updateClassListFromDatabase()
        }
        Log.d("ClassListUpdate", "Current List: ${_classList.value}")
        Log.d("ClassListUpdate", "New Class: $newClass")
    }


    fun updateDropdownDate(date: String) {
        dropdownDate = date
    }


    fun updateIsRegistering() {
        if (isRegistering) {
            isRegistering=false
        }
        else {
            isRegistering=true
        }
    }



//    fun updateStudentId(id: String) {
//        studentId = id
//    }
//
//    fun updateFirstName(name: String) {
//        firstName = name
//    }
//
//    fun updateLastName(name:String) {
//        lastName = name
//    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


    fun recognizeFace(currentEmbedding: FloatArray,className: String,section: String,dropDown:String,firstName: String,lastName: String) {
        Log.d("FaceRecognition", "recognizeFace() is called!")
        viewModelScope.launch(Dispatchers.IO) {
            val faces = faceDao.getAllEmbeddings(className,section)
            Log.d("recoDebug",faces.toString())
            var bestMatch: String? = null
            var highestSimilarity = 0.0

            var fN=""
            var lN=""

            for (face in faces) {
                val similarity = cosineSimilarity(currentEmbedding, face.embedding)
                if (similarity > highestSimilarity && similarity > 0.8) {
                    highestSimilarity = similarity.toDouble()
                    bestMatch = face.firstName+" "+face.lastName
                    fN = face.firstName
                    lN = face.lastName
                }
            }
            val similarityPercentage = (highestSimilarity * 100).toInt() // Convert to percentage

            if (bestMatch != null && fN.isNotEmpty() && lN.isNotEmpty() && (similarityPercentage>=80) ) {
                Log.d("bestMatch","update attendance called")
                Log.d("fn",fN+lN)

                val attendanceRecord = attendanceDao.getAttendanceForDateAndStudent(className, dropDown, firstName = fN,lastName= lN)
                val studentIdFromDetails= faceDao.getStudentIdByDetails(className,section,firstName,lastName)
                val attendanceEntity= AttendanceRecord(studentId = studentIdFromDetails, isPresent = true, date = dropDown )
                if (attendanceRecord == null) {
                    attendanceDao.insertAttendanceRecord(attendanceEntity)
                    loadStudentsWithAttendance(className, date = dropDown)
                }
            }

            Log.d("FaceRecognition", "Best match: ${bestMatch ?: "No match"}")
            withContext(Dispatchers.Main) {
                _recognizedName.value = if (bestMatch != null) {
                    "$bestMatch ($similarityPercentage%)"
                } else {
                    "Unknown"
                }

                Log.d("FaceRecognition", "Updated recognizedName: ${_recognizedName.value}")

            }
        }
    }

    fun registerFace(studentId:String, firstName: String,lastName:String, embedding: FloatArray,faceBitmap: Bitmap,className:String,section:String) {
        val imageBytes = bitmapToByteArray(faceBitmap)
        viewModelScope.launch {
            val studentEntity = StudentEntity(studentId = studentId, firstName = firstName, lastName = lastName, embedding = embedding, image = imageBytes, className = className, section = section )
            val attendanceEntity= AttendanceRecord(studentId = studentId, isPresent = true, date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time) )
            faceDao.insertEmbedding(studentEntity)
            attendanceDao.insertAttendanceRecord(attendanceEntity)
        }
    }

    fun loadFaceEmbeddings(className:String,section: String) {
        Log.d("face loaded run","for $className and $section")
        viewModelScope.launch(Dispatchers.IO) {
            val faces = faceDao.getAllEmbeddings(className,section) // Fetch from Room DB
            _faceEmbeddings.value = faces // Update StateFlow
        }
    }

    fun loadStudentsWithAttendance(className: String, date: String) {
        Log.d("debugData",className+" "+date)
        val parts = className.split(" ")
        Log.d("debugData",parts.toString())
        val className1 = parts.getOrNull(0) ?: ""
        val className2 = parts.getOrNull(1) ?: ""
        val sectionSplit = parts.getOrNull(2) ?: ""

        Log.d("debugData",className1+ " "+className2)
        Log.d("debugData",sectionSplit)
//        Log.d("debugData",sectionSplitt)
        viewModelScope.launch {

            attendanceDao.getStudentsWithAttendance(className1+" "+className2,sectionSplit, date)

                .collect { studentList ->

                    _studentsWithAttendance.value = studentList

                    Log.d("studentsList",studentList.toString())
                }
        }
    }

private fun cosineSimilarity(vec1: FloatArray, vec2: FloatArray): Float {
    val dotProduct = vec1.zip(vec2).sumOf { (a, b) -> (a * b).toDouble() }.toFloat()
    val magnitude1 = kotlin.math.sqrt(vec1.sumOf { it.toDouble() * it.toDouble() }).toFloat()
    val magnitude2 = kotlin.math.sqrt(vec2.sumOf { it.toDouble() * it.toDouble() }).toFloat()

    return if (magnitude1 == 0f || magnitude2 == 0f) 0f else dotProduct / (magnitude1 * magnitude2)
}



















    // Methods to update the state variables

//    fun toggleShowToastAddStudent() {
//        if (showToastAddStudent) {
//            showToastAddStudent=false
//        }
//        else {
//            showToastAddStudent=true
//        }
//    }
//
//    fun toggleShowToastTakeAttedance() {
//        if (showToastTakeAttednance) {
//            showToastTakeAttednance=false
//        }
//        else {
//            showToastTakeAttednance=true
//        }
//    }
//
//
//    fun updateSection(newSection:String) {
//        section=newSection
//    }
//
//    fun updateClassName(newClass:String) {
//        className=newClass
//    }
}
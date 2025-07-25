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
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.data.source.remote.dto.AttendanceDto
import com.example.attendanceappoffline.data.source.remote.dto.StudentDto
import com.example.attendanceappoffline.domain.usecases.AttendanceUseCases
import com.example.attendanceappoffline.domain.usecases.StudentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class FaceRecognitionViewModel @Inject constructor(private val studentUseCases: StudentUseCases, private val attendanceUseCases: AttendanceUseCases) : ViewModel() {

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

//    private val _isRegistering = MutableStateFlow(false)
//    val isRegistering: StateFlow<Boolean> = _isRegistering

//    var isRegistering by  mutableStateOf(false)

    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering.asStateFlow()


    var isCameraAvailable by mutableStateOf(true)

    val calendar = java.util.Calendar.getInstance()


    private val _recognizedName = MutableStateFlow("Unknown")
    val recognizedName: StateFlow<String> = _recognizedName


//    fun updateIsRegistering(value:Boolean) {
//        isRegistering=value
//    }

    fun setRegistering(value: Boolean) {
        _isRegistering.value = value
    }
    fun updateIsCameraAvailable() {
        if (isCameraAvailable) {
            isCameraAvailable=false
        }
        else {
            isCameraAvailable=true
        }
    }

    fun recognizeFace(currentEmbedding: FloatArray, facesFromDB:List<StudentEntity>, className: String, section: String, dropDown:String, schoolId: String) {
        Log.d("FaceRecognition", "recognizeFace() is called!")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val isoString = sdf.format(calendar.time)

        viewModelScope.launch(Dispatchers.IO) {
//            val faces = faceDao.getAllEmbeddings(className,section)
            val faces=facesFromDB
            Log.d("recoDebug",faces.toString())
            var bestMatch: String? = null
            var highestSimilarity = 0.0

            var fN=""
//            var lN=""

            for (face in faces) {
                val similarity = cosineSimilarity(currentEmbedding, face.embedding)
                if (similarity > highestSimilarity && similarity > 0.8) {
                    highestSimilarity = similarity.toDouble()
                    bestMatch = face.fullName
                    fN = face.fullName
                }
            }
            val similarityPercentage = (highestSimilarity * 100).toInt() // Convert to percentage

            if (bestMatch != null && fN.isNotEmpty() && (similarityPercentage>=80) ) {
                Log.d("bestMatch","update attendance called")
                Log.d("fn num","1")


                try {
                    Log.d("className",className+"-"+section)
                    Log.d("date",dropDown)
                    Log.d(("fullName"),fN)
                    val attendanceRecord  = attendanceUseCases.getAttendanceForDateAndStudent(className = className+"-"+section, date = dropDown,fullName=fN,schoolId = schoolId )
                    val studentIdFromDetails=studentUseCases.getStudentIdByDetails(className = className+"-"+section,fullName = fN,schoolId = schoolId)
                    Log.d("fn id",studentIdFromDetails ?: "null")
                    Log.d("dropdown",dropDown)
                    Log.d("student Id",studentIdFromDetails)
                    val attendanceEntity = AttendanceRecord(studentHash = studentIdFromDetails, status = "present", date = dropDown, schoolId = schoolId , className = className+"-"+section, isSynced = false )
                    Log.d("fn","4")
                    if (attendanceRecord == null) {
                        Log.d("log","log")
                        attendanceUseCases.insertAttendanceRecord(attendanceEntity)
                        attendanceUseCases.getStudentsWithAttendance(className,dropDown)
                        attendanceUseCases.addAttendanceToDB(dto = AttendanceDto(studentHash = attendanceEntity.studentHash, date = isoString, status = "present",schoolId = attendanceEntity.schoolId, className = attendanceEntity.className,isSynced = true))
                    }
                }
                catch (err:Exception) {
                    Log.d("err",err.toString())
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

    fun registerFace(studentId:String, fullName: String, rollNumber:String, embedding: FloatArray, faceBitmap: Bitmap, className:String, section:String,schoolId:String,date:String) {
        val imageBytes = bitmapToByteArray(faceBitmap)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val isoString = sdf.format(calendar.time)
        Log.d("date",SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time))
        viewModelScope.launch {
            val studentEntity = StudentEntity(studentHash = studentId, fullName = fullName, rollNumber = rollNumber, embedding = embedding, image = imageBytes, className = className+"-"+section, schoolId = schoolId )
            val attendanceEntity= AttendanceRecord(studentHash = studentId, status = "present", date = date, className = className+"-"+section, schoolId = schoolId )
            studentUseCases.insertEmbedding(studentEntity)
            attendanceUseCases.insertAttendanceRecord(attendanceEntity)
            studentUseCases.saveStudentToDB(StudentDto(studentHash = studentEntity.studentHash, fullName = studentEntity.fullName, className = studentEntity.className, rollNumber = studentEntity.rollNumber, schoolId = studentEntity.schoolId))
            Log.d("df","df")
            attendanceUseCases.addAttendanceToDB(AttendanceDto(studentHash = studentId,date=isoString, status = "present",schoolId=schoolId,className=className+"-"+section,isSynced = true))
            Log.d("fd","fd")
        }
    }

    private fun cosineSimilarity(vec1: FloatArray, vec2: FloatArray): Float {
        val dotProduct = vec1.zip(vec2).sumOf { (a, b) -> (a * b).toDouble() }.toFloat()
        val magnitude1 = kotlin.math.sqrt(vec1.sumOf { it.toDouble() * it.toDouble() }).toFloat()
        val magnitude2 = kotlin.math.sqrt(vec2.sumOf { it.toDouble() * it.toDouble() }).toFloat()

        return if (magnitude1 == 0f || magnitude2 == 0f) 0f else dotProduct / (magnitude1 * magnitude2)
    }







    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}

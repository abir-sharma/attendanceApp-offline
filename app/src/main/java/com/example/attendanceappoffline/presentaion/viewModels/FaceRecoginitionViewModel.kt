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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale


class FaceRecognitionViewModel(private val faceDao: StudentsDao, private val attendanceDao: AttendanceDao) : ViewModel() {

    var isRegistering by  mutableStateOf(false)
    var isCameraAvailable by mutableStateOf(true)

    val calendar = java.util.Calendar.getInstance()


    private val _recognizedName = MutableStateFlow("Unknown")
    val recognizedName: StateFlow<String> = _recognizedName


    fun updateIsRegistering(value:Boolean) {
        isRegistering=value
    }

    fun updateIsCameraAvailable() {
        if (isCameraAvailable) {
            isCameraAvailable=false
        }
        else {
            isCameraAvailable=true
        }
    }

    fun recognizeFace(currentEmbedding: FloatArray, facesFromDB:List<StudentEntity>, className: String, section: String, dropDown:String, firstName: String, lastName: String) {
        Log.d("FaceRecognition", "recognizeFace() is called!")
        viewModelScope.launch(Dispatchers.IO) {
//            val faces = faceDao.getAllEmbeddings(className,section)
            val faces=facesFromDB
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
                Log.d("fn num","1")


                try {
                    val attendanceRecord = attendanceDao.getAttendanceForDateAndStudent(className, dropDown, firstName = fN,lastName= lN)
                    Log.d("fn reco",attendanceRecord.toString())
                    Log.d("fn",className)
                    Log.d("fn",section)
                    Log.d("fn",firstName)
                    Log.d("fn",lastName)
                    Log.d("fn",fN+ " "+lN)
                    val studentIdFromDetails= faceDao.getStudentIdByDetails(className,section,fN,lN)
                    Log.d("fn id",studentIdFromDetails ?: "null")
                    Log.d("dropdown",dropDown)
                    val attendanceEntity = AttendanceRecord(studentId = studentIdFromDetails, isPresent = true, date = dropDown )
                    Log.d("fn","4")
                    if (attendanceRecord == null) {
                        Log.d("fn","5")
                        attendanceDao.insertAttendanceRecord(attendanceEntity)
                        Log.d("fn","6")
                        attendanceDao.getStudentsWithAttendance(className,section,dropDown)
                        Log.d("fn","7")
//                    loadStudentsWithAttendance(className, date = dropDown)  this function will be from attendance view Model
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

    fun registerFace(studentId:String, firstName: String, lastName:String, embedding: FloatArray, faceBitmap: Bitmap, className:String, section:String) {
        val imageBytes = bitmapToByteArray(faceBitmap)
        viewModelScope.launch {
            val studentEntity = StudentEntity(studentId = studentId, firstName = firstName, lastName = lastName, embedding = embedding, image = imageBytes, className = className, section = section )
            val attendanceEntity= AttendanceRecord(studentId = studentId, isPresent = true, date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time) )
            faceDao.insertEmbedding(studentEntity)
            attendanceDao.insertAttendanceRecord(attendanceEntity)
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

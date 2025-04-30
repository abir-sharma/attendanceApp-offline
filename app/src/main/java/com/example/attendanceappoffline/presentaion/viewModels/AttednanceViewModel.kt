import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceappoffline.data.StudentWithAttendance
import com.example.attendanceappoffline.data.attendance.AttendanceDao
import com.example.attendanceappoffline.domain.models.AttendanceRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AttendanceViewModel(private val attendanceDao: AttendanceDao) : ViewModel() {


    var startAttendance by mutableStateOf(false)
    var showToastTakeAttednance by mutableStateOf(false)


    private val _studentsWithAttendance = MutableStateFlow<List<StudentWithAttendance>>(emptyList())
    val studentsWithAttendance: StateFlow<List<StudentWithAttendance>> = _studentsWithAttendance

    val attendanceStats = mutableMapOf(
        "presentCount" to "0",
        "absentCount" to "0",
        "percentage" to "0"
    )



    fun loadStudentsWithAttendance(className: String,section:String, date: String) {
        Log.d("debugDataatt",className+" "+date+" "+section)
        Log.d("className",className)
        Log.d("data",date)
        Log.d("section",section)
        viewModelScope.launch {
            attendanceDao.getStudentsWithAttendance(className,section,date)
                .collect { studentList ->
                    _studentsWithAttendance.value = studentList
                    calculateAttendanceStats(_studentsWithAttendance.value)
                    Log.d("studentsListatt",studentList.toString())
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

        attendanceStats["presentCount"] = presentCount.toString()
        attendanceStats["absentCount"] = absentCount.toString()
        attendanceStats["percentage"] = percentage.toString()

        Log.d("AttendanceStats", attendanceStats.toString())
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

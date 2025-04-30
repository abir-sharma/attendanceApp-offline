import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.attendanceappoffline.data.source.local.dao.StudentsDao

//import com.ml.quaterion.facenetdetection.R

class StudentViewModel(private val studentsDao: StudentsDao) : ViewModel() {

    var AddStudentForm by mutableStateOf(false)
    var showToastAddStudent by mutableStateOf(false)

    var studentId by  mutableStateOf("")
    var firstName by  mutableStateOf("")
    var lastName by  mutableStateOf("")
    var className by mutableStateOf("")
    var section by mutableStateOf("")


    fun updateStudentId(id: String) {
        studentId = id
    }

    fun updateFirstName(name:String) {
        firstName=name
    }

    fun updateLastName(name:String) {
        lastName=name
    }

    fun updateSection(newSection:String) {
        section=newSection
    }

    fun updateClassName(newClass:String) {
        className=newClass
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

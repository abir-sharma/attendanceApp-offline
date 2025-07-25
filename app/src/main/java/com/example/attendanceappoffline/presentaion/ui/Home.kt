package com.example.attendanceappoffline.presentaion.ui

//import EmbeddedActivityScreen
import Navbar
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.attendanceappoffline.common.LoginPreferenceManager
//import com.example.attendanceappoffline.R
//import com.ml.quaterion.facenetdetection.R
import com.example.attendanceappoffline.domain.models.Student
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AttendanceViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModel
import com.example.attendanceappoffline.utils.AddStudentForm
import com.example.attendanceappoffline.utils.CameraCard
import com.example.attendanceappoffline.utils.RequestCameraPermission
import com.example.attendanceappoffline.utils.StudentCard
import com.example.attendanceappoffline.utils.SummaryCard
import com.example.attendanceappoffline.utils.Toast
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.delay
import okhttp3.WebSocket
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("ContextCastToActivity")

@Composable
fun Home(
    studentViewModel: StudentViewModel,
    attendanceViewModel: AttendanceViewModel,
    globalStateViewModel: GlobalStateViewModel,
    faceRecognitionViewModel: FaceRecognitionViewModel,
    authViewModel: AuthViewModel
    ) {

    var showDialog by remember { mutableStateOf(false) } // State for dialog visibility
    val calendar = Calendar.getInstance()

//    val students by globalStateViewModel.studentsWithAttendance.collectAsState()
    val students by attendanceViewModel.studentsWithAttendance.collectAsState()

    val context = LocalContext.current
    val loginPrefs = remember { LoginPreferenceManager(context) }
    val isLoggedIn by loginPrefs.isLoggedIn.collectAsState(initial = false)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            authViewModel.loadSchoolId(loginPrefs)
        }
    }
    val schoolId by authViewModel.schoolId.collectAsState()

    Log.d("schoolIdHome",schoolId)
//    var selectedDate by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time)) }
//    var selectedClassNameWithSection by remember { mutableStateOf("Select Class") }

    val selectedClassNameWithSection = studentViewModel.selectedClassNameWithSection
    val selectedDate = studentViewModel.selectedDate



    // Trigger data load when class/date changes
    LaunchedEffect(selectedClassNameWithSection, selectedDate) {
        val parts = selectedClassNameWithSection.split("-")
        val className = parts.getOrNull(0) ?: ""
        val section = parts.getOrNull(1) ?: ""

        globalStateViewModel.updateDropdownDate(date = selectedDate)

//        attendanceViewModel.loadStudentsWithAttendance(className = className+"-"+section , date = selectedDate)
        attendanceViewModel.loadStudentsWithAttendance(className = selectedClassNameWithSection , date = selectedDate)
        globalStateViewModel.loadFaceEmbeddings(className=className+"-"+section,schoolId)


    }



//    val classList by globalStateViewModel.classList.collectAsState()
    // this function returns list of classes in a school from db unique sorted and with hyphen (-)
    LaunchedEffect(Unit) {
        globalStateViewModel.updateClassListFromDatabase()
    }

    DisposableEffect(Unit) {
        val options = IO.Options().apply {
            reconnection = true
            reconnectionAttempts = 5
            reconnectionDelay = 1000
            timeout = 5000
            query = "schoolId=$schoolId"
            transports= arrayOf(io.socket.engineio.client.transports.WebSocket.NAME)
        }

        val socket = IO.socket("http://172.30.208.1:4100", options)

        socket.on(Socket.EVENT_CONNECT) {
            Log.d("Socket", "Connected")
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("Socket", "Connection Error: ${it[0]}")
        }
        socket.on("deviceCount") {
            Log.d("Socket", "Device count received: ${it[0]}")
        }
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("Socket", "Connected to server")
            socket.emit("join", schoolId)
        }
        socket.connect()
        onDispose {
            socket.emit("leave", schoolId)
            socket.disconnect()
        }
    }

    RequestCameraPermission()  // 🔒 Ask permission once

    Column(
         modifier = Modifier
             .fillMaxSize()
             .background(
                 brush = Brush.horizontalGradient(
                     colors = listOf(Color(0xFFDEDEF4), Color(0xFFF3E3D8))
                 )
             )
     ) {
         Navbar(
             selectedDate = selectedDate,
//             onSelectedDateChange = { newDate -> selectedDate = newDate },
             selectedClassName = selectedClassNameWithSection,
//             onSelectedClassNameChange = { newClass -> selectedClassNameWithSection = newClass },
             globalStateViewModel,
             studentViewModel,
             authViewModel
             )
         Column {
             Row(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.Transparent)

                     .padding(24.dp)
             ) {
                 Column(
                     modifier = Modifier
                         .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
                         .fillMaxWidth(0.35f)
                         .fillMaxHeight(),
                     verticalArrangement = Arrangement.SpaceBetween
                 ) {
                      CameraCard(attendanceViewModel,studentViewModel,globalStateViewModel,faceRecognitionViewModel, authViewModel = authViewModel)

                      SummaryCard(studentViewModel,selectedDate,selectedClassNameWithSection,attendanceViewModel)
                 }
                 Spacer(modifier = Modifier.width(24.dp))
                 Column(
                     modifier = Modifier
//                         .shadow(8.dp, shape = RoundedCornerShape(12.dp)) // Add shadow with rounded corners
                         .background(
                             shape = RoundedCornerShape(16.dp),
                             brush = Brush.horizontalGradient(
                                 colors = listOf(
                                     Color.White.copy(alpha = 0.5f), // 50% visible white on left
                                     Color.White.copy(alpha = 0.7f)  // 70% visible white on right
                                 )
                             )
                         )
                         .border(1.dp, Color.Transparent, RoundedCornerShape(16.dp))
                         .fillMaxSize()
                         .padding(8.dp)
                 ) {
                     Spacer(modifier = Modifier.height(12.dp))
                     Row(modifier = Modifier.fillMaxWidth()) {
                         Spacer(modifier = Modifier.width(8.dp))
                         Text(text = "Students", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

                     }
                     Spacer(modifier = Modifier.height(12.dp))
                     if (students.isNotEmpty()) {
                         LazyVerticalGrid(
                             columns = GridCells.Fixed(5), // 5 elements per row
                             modifier = Modifier
                                 .fillMaxSize()
                                 .padding(0.dp)
                         ) {
                             items(students) { item ->
                                 Log.d("students",students.toString())
                                 Box(
                                     modifier = Modifier
                                         .padding(8.dp)
                                         .aspectRatio(1f) // Keeps items square
                                         .background(
                                             Color.White,
                                             shape = RoundedCornerShape(16.dp)
                                         )
                                         .border(0.5.dp, Color.Gray, RoundedCornerShape(16.dp)),
                                     contentAlignment = Alignment.Center
                                 ) {
//                                 StudentCard(attendnace=item,studentViewModel)
                                     StudentCard(item)
                                 }
                             }
                         }
                     }
                     else {
                         Column(
                             modifier = Modifier.fillMaxSize(),
                             horizontalAlignment = Alignment.CenterHorizontally,
                             verticalArrangement = Arrangement.Center
                         ) {
                             Text(text = "No Data Found !", fontWeight = FontWeight.SemiBold, fontSize = 24.sp, color = Color.LightGray)
                         }
                     }
                     
                 }

             }
         }
     }

    // Add Student Dialog

    if (studentViewModel.AddStudentForm) {
        Dialog(onDismissRequest = { showDialog = false }) {
            AddStudentForm(studentViewModel,globalStateViewModel,
                faceRecognitionViewModel,
                authViewModel,
                selectedClassNameWithSection = selectedClassNameWithSection,
//                onSelectedClassNameChange = { newClass -> selectedClassNameWithSection = newClass }
                )
        }
    }

    if (studentViewModel.showToastAddStudent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Toast(aboveText = "", lowerText = "Student Added Successfully !",attendanceViewModel,studentViewModel)
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
//            globalStateViewModel.toggleShowToastAddStudent()
            studentViewModel.closeToastAddStudent(false)

        }, 3000) // 3000 ms = 3 seconds
    }

    // Toast at Bottom Right Corner
    if (attendanceViewModel.showToastTakeAttednance)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
                Toast(aboveText = "Taking Attendance", lowerText = "Please take the camera steady.",attendanceViewModel,studentViewModel)
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
              attendanceViewModel.closeTakeAttendanceToast(false)
        }, 3000) // 3000 ms = 3 seconds

    }









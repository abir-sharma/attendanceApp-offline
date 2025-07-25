package com.example.attendanceappoffline.utils

import CameraPreview
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.attendanceappoffline.R
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AttendanceViewModel
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
//@Preview(
//    name = "Landscape Preview",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
//    device = "spec:width=800dp,height=400dp,dpi=240"
//)
fun CameraCard(attendanceViewModel: AttendanceViewModel,studentViewModel: StudentViewModel,globalStateViewModel: GlobalStateViewModel,faceRecognitionViewModel: FaceRecognitionViewModel,authViewModel: AuthViewModel) {

    var showUI by remember { mutableStateOf(false) } // State to control visibility
    val recognizedName by faceRecognitionViewModel.recognizedName.collectAsState()
//    val faceRects by globalStateViewModel.faceBounds.collectAsState()
    val isFaceMatched = recognizedName=="Unknown"

    // Delay UI display for 3 seconds
    LaunchedEffect(Unit) {
        delay(1)  // 3-second delay
        showUI = true
    }

    Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.625f)
//                .height(500.dp)
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically )
            {
                Text(text = "Facial Recoginition Camera", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                if (attendanceViewModel.startAttendance) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { attendanceViewModel.stopStartAttendnace(false) },
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Box(modifier = Modifier
                                .size(15.dp)
                                .background(Color.White, shape = RoundedCornerShape(2.dp)))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Stop", color = Color.White)
                        }
                    }
                    
                }

            }

            Box(modifier = Modifier
                .background(Color(0xFFE3DFED), shape = RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
//                .height(100.dp),
                contentAlignment = Alignment.Center // Centers the content inside

            ) {
                if (attendanceViewModel.startAttendance) {
//                    FaceDetectionScreen(globalStateViewModel,attendanceViewModel)
                    if (attendanceViewModel.startAttendance) {
                        CameraPreview(globalStateViewModel,attendanceViewModel,studentViewModel,faceRecognitionViewModel, authViewModel = authViewModel)
                    }
                    CornerOnlyBorderBoxWithScanner()
                }
                else {
                    TakeAttendance(attendanceViewModel,globalStateViewModel)
                }
            }
            Row(modifier = Modifier.height(64.dp), verticalAlignment = Alignment.CenterVertically ) {
                if (attendanceViewModel.startAttendance) {
                    if (showUI) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(text = recognizedName , fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            Button(
                                onClick = { studentViewModel.openAddStudentForm(true) },
//                                colors = ButtonDefaults.buttonColors(Color(0xFF1570EF))
                                enabled = isFaceMatched,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isFaceMatched) Color(0xFF1570EF) else Color.Gray
                                )
                            ) {
                                Text(text = "Add Student", color = Color.White)
                            }
                        }

                    }

                }
                else {
                    Text(text = "Please stand in front of Webcam in a way\n" +
                            "such that your face must be clearly visible", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
}



@Composable
fun TakeAttendance(attendanceViewModel: AttendanceViewModel,globalStateViewModel: GlobalStateViewModel) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val selectedDate = try {
        dateFormat.parse(globalStateViewModel.dropdownDate)
    } catch (e: Exception) {
        null
    }

    val currentDate = dateFormat.parse(dateFormat.format(Date()))

    val isTodayOrFuture = selectedDate != null && currentDate != null && !selectedDate.before(currentDate)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE3DFED))
            .padding(horizontal = 24.dp), // Background color
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp)), // White circular background
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(35.dp) // Set size of the circle
                    .clip(CircleShape) // Make it circular
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between icon and button

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                attendanceViewModel.startStartAttednance(true)
                attendanceViewModel.openTakeAttendanceToast(true)

//                globalStateViewModel.updateAboveText("Taking Attendance")
//                globalStateViewModel.updateLowerText("Please take the camera steady.")
//                globalStateViewModel.toggleShowToastTakeAttedance()
                      },
//            colors = ButtonDefaults.buttonColors(Color(0xFF1570EF))
            enabled = isTodayOrFuture,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTodayOrFuture) Color(0xFF1570EF) else Color.Gray
            )
        ) {
            Text(text = "Start Attendance", color = Color.White)
        }
    }
}


@Composable
fun CornerOnlyBorderBoxWithScanner() {
    val boxSize = 110.dp
    val infiniteTransition = rememberInfiniteTransition()

    // Access LocalDensity to convert Dp to pixels
    val density = LocalDensity.current
    val boxHeightPx = with(density) { boxSize.toPx() } // Convert Dp to Px

    // Animate the scan line vertically within the box size
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = boxHeightPx, // Ensure the target value stays within the box height
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(boxSize)
            .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
//            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val cornerLength = 10.dp.toPx()
                val color = Color.Blue

                // Draw corners-only border
                drawLine(color, Offset(0f, 0f), Offset(cornerLength, 0f), strokeWidth)
                drawLine(color, Offset(0f, 0f), Offset(0f, cornerLength), strokeWidth)

                drawLine(
                    color,
                    Offset(size.width, 0f),
                    Offset(size.width - cornerLength, 0f),
                    strokeWidth
                )
                drawLine(
                    color,
                    Offset(size.width, 0f),
                    Offset(size.width, cornerLength),
                    strokeWidth
                )

                drawLine(
                    color,
                    Offset(0f, size.height),
                    Offset(cornerLength, size.height),
                    strokeWidth
                )
                drawLine(
                    color,
                    Offset(0f, size.height),
                    Offset(0f, size.height - cornerLength),
                    strokeWidth
                )

                drawLine(
                    color,
                    Offset(size.width, size.height),
                    Offset(size.width - cornerLength, size.height),
                    strokeWidth
                )
                drawLine(
                    color,
                    Offset(size.width, size.height),
                    Offset(size.width, size.height - cornerLength),
                    strokeWidth
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, scanLineY),
                    end = Offset(size.width, scanLineY),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}

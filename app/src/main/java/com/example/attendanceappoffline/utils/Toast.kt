package com.example.attendanceappoffline.utils

import AttendanceViewModel
import StudentViewModel
import android.os.Handler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel

@Composable
fun Toast(aboveText:String,lowerText:String,attendanceViewModel: AttendanceViewModel,studentViewModel: StudentViewModel) {
    Row(
      modifier = Modifier
          .background(Color.White)
          .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(45.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.size(40.dp).fillMaxHeight().background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
                ) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(20.dp)
                        .background(shape = RoundedCornerShape(24.dp), color = Color.White)
                )
            }

            Column {
                if (aboveText.isNotEmpty()) {
                    Text(text = aboveText)
                }
                Text(text = lowerText)
            }
        }

        Icon(imageVector = Icons.Default.Clear, contentDescription ="cross", modifier = Modifier.size(30.dp)
            .clickable {
                // Toggle the showToast state
                if (aboveText.isNotEmpty()) {
                    attendanceViewModel.closeTakeAttendanceToast(false)
                }
                else {
                    studentViewModel.closeToastAddStudent(false)
                }
            }
        )
    }
}
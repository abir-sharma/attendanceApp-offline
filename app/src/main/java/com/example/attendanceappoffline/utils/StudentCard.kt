package com.example.attendanceappoffline.utils

import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendanceappoffline.data.StudentWithAttendance


@Composable
fun StudentCard(studentWithAttendance: StudentWithAttendance) {
    // UI colors based on presence
    val borderColor = if (studentWithAttendance.isPresent == true) Color(0xFF73E2A3) else Color(0xFFFDA29B)
    val studentNameColor = if (studentWithAttendance.isPresent == true) Color(0xFF099250) else Color(0xFFD92D20)
    val studentNameBackgroundColor = if (studentWithAttendance.isPresent == true) Color(0xFFD3F8DF) else Color(0xFFFEE4E2)
    val attendanceStatus = if (studentWithAttendance.isPresent == true) "Present" else "Absent"

    // SAFELY load image using side-effect
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(studentWithAttendance.student.image) {
        try {
            studentWithAttendance.student.image?.let { byteArray ->
                Log.d("ImageDebug", "Image size: ${byteArray.size}")
                val bitmap = byteArrayToBitmap(byteArray)
                imageBitmap = bitmap?.asImageBitmap()
            }
        } catch (e: Exception) {
            Log.e("ImageError", "Failed to convert image", e)
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .width(140.dp)
            .height(130.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (studentWithAttendance.student != null) {
            imageBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Student Image",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(2.dp, borderColor, CircleShape)
                )
            } ?: run {
                Log.e("ImageError", "ImageBitmap is null, skipping Image()")
            }

            Row {
                Text(
                    text = studentWithAttendance.student.firstName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = studentWithAttendance.student.lastName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(studentNameBackgroundColor, shape = RoundedCornerShape(16.dp))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = attendanceStatus,
                fontSize = 12.sp,
                color = studentNameColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
    return try {
        if (byteArray == null || byteArray.isEmpty()) return null
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } catch (e: Exception) {
        Log.e("ImageError", "Bitmap decoding failed", e)
        null
    }
}


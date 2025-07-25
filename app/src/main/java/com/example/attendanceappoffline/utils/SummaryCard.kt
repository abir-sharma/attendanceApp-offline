package com.example.attendanceappoffline.utils

//import AttendanceViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AttendanceViewModel
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModel
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
//@Preview(
//    name = "Landscape Preview",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
//    device = "spec:width=800dp,height=400dp,dpi=240"
//)
fun SummaryCard(studentViewModel: StudentViewModel,selectedDate:String,selectedClassName:String,attendanceViewModel: AttendanceViewModel) {
//    val attendanceStats=studentViewModel.calculateAttendanceStats(selectedDate,selectedClassName)
    val pC=attendanceViewModel.presentCountt
    val aC=attendanceViewModel.absentCountt
    val perc=attendanceViewModel.percentagee
    val rows= listOf(
//        attendanceStats["presentCount"]?.let { ROW("Present", it) },
//        attendanceStats["absentCount"]?.let { ROW("Absent", it) },
//        attendanceStats["percentage"]?.let { ROW("Overall Attendance", it) },
        ROW("Present",pC),
        ROW("Absent",aC),
        ROW("Overall Attendance",perc)
    )
    Column(
         modifier = Modifier
             .clip(RoundedCornerShape(8.dp))
             .border(
                 1.dp,
                 Color.Transparent,
                 shape = RoundedCornerShape(12.dp)
             ) // Border with rounded corners
//             .shadow(8.dp, shape = RoundedCornerShape(12.dp)) // Add shadow with rounded corners
             .background(shape = RoundedCornerShape(16.dp),
                 brush = Brush.horizontalGradient(
                     colors = listOf(
                         Color.White.copy(alpha = 0.5f), // 50% visible white on left
                         Color.White.copy(alpha = 0.7f)  // 70% visible white on right
                     )
                 ))
             .fillMaxWidth()
             .height(200.dp)
             .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Text(text = "Today's Summary", fontSize = 22.sp, fontWeight = FontWeight.SemiBold )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rows) { item ->
                if (item != null) {
                    SingleRow(leftText = item.leftText, rightText =item.rightText, )
                }
            }
        }

    }
}

@Composable
fun SingleRow(leftText:String,rightText:String) {
    val color= if (leftText.equals("Overall Attendance")) Color(0xFF099250) else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = leftText,color = Color.Gray,fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(text = rightText,fontSize = 20.sp,color= color, fontWeight = FontWeight.SemiBold)
    }
}

data class ROW(val leftText: String,val rightText: String)
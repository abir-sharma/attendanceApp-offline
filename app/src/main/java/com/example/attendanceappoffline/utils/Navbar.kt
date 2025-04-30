import android.app.DatePickerDialog
import android.content.res.Configuration
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(
//    name = "Landscape Preview",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
//    device = "spec:width=800dp,height=400dp,dpi=240"
//)


@Composable
fun Navbar(selectedDate: String,
           onSelectedDateChange: (String) -> Unit,
           selectedClassName: String,
           onSelectedClassNameChange: (String) -> Unit,
           globalStateViewModel: GlobalStateViewModel,
           studentViewModel: StudentViewModel
) {
//    var selectedClass by remember { mutableStateOf("Select Class") }

    val calendar = Calendar.getInstance()
    val classList by globalStateViewModel.classList.collectAsState()
    Log.d("class",classList.toString())
//    var selectedDate by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time)) }
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time)
            onSelectedDateChange(formattedDate)
            globalStateViewModel.updateDropdownDate(formattedDate) // <-- ✅ this updates ViewModel

        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.4f))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var expanded by remember { mutableStateOf(false) }
        Text(text = "Attendance System", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Row {
            Box {
                Button(onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // Set the background color to white
                        contentColor = Color.Black // Set the text and icon color to black
                    ),
                    modifier = Modifier
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) // Less rounded corners
                        .border(
                            1.dp,
                            Color.LightGray,
                            RoundedCornerShape(8.dp)
                        ) // Thin light gray border
                        .padding(0.dp), // Increase inner padding
                    ) {
                    Text(selectedClassName)
                    Spacer(modifier = Modifier
                        .width(24.dp)
                        .background(Color.White))
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Select Class")

                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    classList.forEach { className ->
                        DropdownMenuItem(
                            text = { Text(className) },
                            onClick = {
//                                selectedClass = className
                                onSelectedClassNameChange(className)
                                val parts = className.split(" ")

                                val sectionSplit = parts.last() // "B"
                                val classNameSplit = parts.dropLast(1).joinToString(" ")
                                studentViewModel.updateClassName(classNameSplit)
                                studentViewModel.updateSection(sectionSplit)
//                                globalStateViewModel.updateClassName(className)
                                expanded = false
                            }
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = { datePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Set the background color to white
                    contentColor = Color.Black // Set the text and icon color to black
                ),
                modifier = Modifier
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ) // Less rounded corners
                    .border(
                        1.dp,
                        Color.LightGray,
                        RoundedCornerShape(8.dp)
                    ) // Thin light gray border
                    .padding(0.dp), // Increase inner padding
                )
            {
                Text(selectedDate)
                Spacer(modifier = Modifier
                    .width(12.dp)
                    .background(Color.White))
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")

            }
        }
    }
}

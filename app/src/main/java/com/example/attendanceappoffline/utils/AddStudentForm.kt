package com.example.attendanceappoffline.utils

import StudentViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendanceappoffline.domain.models.Student
import com.example.attendanceappoffline.R
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
//import com.ml.quaterion.facenetdetection.R
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddStudentForm(studentViewModel: StudentViewModel,globalStateViewModel: GlobalStateViewModel,
                   faceRecognitionViewModel: FaceRecognitionViewModel,
                   selectedClassNameWithSection: String,
                   onSelectedClassNameChange: (String) -> Unit, ) {

    Log.d("clssNameWithSection",selectedClassNameWithSection)
    val classNameFromNavBar:String
    val sectionFromNavBar:String
    if (selectedClassNameWithSection.equals("Select Class")) {
        classNameFromNavBar=""
        sectionFromNavBar=""
    }
    else {
        val parts = selectedClassNameWithSection.split(" ")
        classNameFromNavBar = parts.subList(0, parts.size - 1).joinToString(" ") // "12th Class"
        sectionFromNavBar = parts.last()
    }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf(classNameFromNavBar) }
    var section by remember { mutableStateOf(sectionFromNavBar) }
    var expanded by remember { mutableStateOf(false) }

    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var classError by remember { mutableStateOf(false) }
    var sectionError by remember { mutableStateOf(false) }


    val classListAddStudent = listOf(
        "Select Class",
        "1st Class",
        "2nd Class",
        "3rd Class",
        "4th Class",
        "5th Class",
        "6th Class",
        "7th Class",
        "8th Class",
        "9th Class",
        "10th Class",
        "11th Class",
        "12th Class"    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier
            .background(Color.White)
            .padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Enter Student Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                IconButton(onClick = {studentViewModel.closeAddStudentForm(false)}) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", Modifier.size(30.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // First Name & Last Name
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it; firstNameError = it.isBlank() },
                    label = { Text("First Name") },
                    isError = firstNameError,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it; lastNameError = it.isBlank() },
                    label = { Text("Last Name") },
                    isError = lastNameError,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Class & Section
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = selectedClass,
                        onValueChange = {
                            selectedClass = it
                            classError = false
                        },
                        label = { Text("Class") },
                        readOnly = true,
                        isError = classError,
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", modifier = Modifier.clickable { expanded = true })
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

                        classListAddStudent.forEach { className ->
//                            val parts=className.split(" ")
                            DropdownMenuItem(text = { Text(className) }, onClick = {
                                selectedClass = className
                                expanded = false
                                classError = false
                            })
                        }
                    }
                }
                OutlinedTextField(
                    value = section,
                    onValueChange = { section = it; sectionError = it.isBlank() },
                    label = { Text("Section") },
                    isError = sectionError,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add Student Button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        // Validate Fields
                        firstNameError = firstName.isBlank()
                        lastNameError = lastName.isBlank()
                        classError = selectedClass == "Select Class"
                        sectionError = section.isBlank()

                        if (!firstNameError && !lastNameError && !classError && !sectionError) {
                            globalStateViewModel.addClassNameIfNotExists(selectedClass+" "+section.uppercase())

                            studentViewModel.updateStudentId(System.currentTimeMillis().toString())
                            studentViewModel.updateFirstName(firstName)
                            studentViewModel.updateLastName(lastName)
                            studentViewModel.updateClassName(newClass = selectedClass)
                            studentViewModel.updateSection(newSection=section.uppercase())

                            onSelectedClassNameChange(selectedClass + " " + section.uppercase())
//                            globalStateViewModel.updateIsRegistering()
                            faceRecognitionViewModel.updateIsRegistering(true)
                            studentViewModel.openToastAddStudent(true)
                            studentViewModel.closeAddStudentForm(false)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF1570EF))
                ) {
                    Text(text = "Add Student", color = Color.White)
                }
            }
        }
    }
}

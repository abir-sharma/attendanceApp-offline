package com.example.attendanceappoffline.utils

import android.graphics.Bitmap
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendanceappoffline.domain.models.Student
import com.example.attendanceappoffline.R
import com.example.attendanceappoffline.common.LoginPreferenceManager
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
//import com.ml.quaterion.facenetdetection.R
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddStudentForm(studentViewModel: StudentViewModel,globalStateViewModel: GlobalStateViewModel,
                   faceRecognitionViewModel: FaceRecognitionViewModel,authViewModel: AuthViewModel,
                   selectedClassNameWithSection: String,
//                   onSelectedClassNameChange: (String) -> Unit,
                   ) {

    Log.d("clssNameWithSection",selectedClassNameWithSection)
    val classNameFromNavBar:String
    val sectionFromNavBar:String
    if (selectedClassNameWithSection.equals("Select Class")) {
        classNameFromNavBar=""
        sectionFromNavBar=""
    }
    else {
        val parts = selectedClassNameWithSection.split("-")
        classNameFromNavBar = parts.getOrNull(0) ?: ""
        sectionFromNavBar = parts.getOrNull(1) ?: ""
//        classNameFromNavBar = parts.subList(0, parts.size - 1).joinToString(" ") // "12th Class"
//        sectionFromNavBar = parts.last()
    }

    var fullName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf(classNameFromNavBar) }
    var section by remember { mutableStateOf(sectionFromNavBar) }
    var classExpanded by remember { mutableStateOf(false) }
    var sectionExpanded by remember { mutableStateOf(false) }


    var fullNameError by remember { mutableStateOf(false) }
    var rollNumberError by remember { mutableStateOf(false) }
    var classError by remember { mutableStateOf(false) }
    var sectionError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val loginPrefs = remember { LoginPreferenceManager(context) }
    val isLoggedIn by loginPrefs.isLoggedIn.collectAsState(initial = false)
//
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            authViewModel.getClassesLocally()
        }
    }
    val classList by authViewModel.classNames.collectAsState()
    val schoolId by authViewModel.schoolId.collectAsState()

    val firstParts = classList.mapNotNull { it.split("-").getOrNull(0) }.distinct()
    val secondParts = classList.mapNotNull { it.split("-").getOrNull(1) }.distinct()
    Log.d("firstParts",firstParts.toString())
    Log.d(("secondParts"),secondParts.toString())
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
                    value = fullName,
                    onValueChange = { fullName = it; fullNameError = it.isBlank() },
                    label = { Text("Full Name") },
                    isError = fullNameError,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = rollNumber,
                    onValueChange = { rollNumber = it; rollNumberError = it.isBlank() },
                    label = { Text("Roll Number") },
                    isError = rollNumberError,
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
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", modifier = Modifier.clickable { classExpanded = true })
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = classExpanded, onDismissRequest = { classExpanded = false }) {

                        firstParts.forEach { className ->
                            DropdownMenuItem(text = { Text(className) }, onClick = {
                                selectedClass = className
                                classExpanded = false
                                classError = false
                            })
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {

                    OutlinedTextField(
                        value = section,
                        onValueChange = {
                            section = it
                            sectionError = false
                        },
                        label = { Text("Section") },
                        readOnly = true,
                        isError = sectionError,
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", modifier = Modifier.clickable { sectionExpanded=true})
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = sectionExpanded, onDismissRequest = { sectionExpanded = false }) {

                        secondParts.forEach { sectionD ->
                            DropdownMenuItem(text = { Text(sectionD) }, onClick = {
                                section = sectionD
                                sectionExpanded = false
                                sectionError = false
                            })
                        }
                    }
                }
//                OutlinedTextField(
//                    value = section,
//                    onValueChange = { section = it; sectionError = it.isBlank() },
//                    label = { Text("Section") },
//                    isError = sectionError,
//                    modifier = Modifier.weight(1f)
//                )
//                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//
//                    secondParts.forEach { sectionD ->
//                        DropdownMenuItem(text = { Text(sectionD) }, onClick = {
//                            section = sectionD
//                            expanded = false
//                            sectionError = false
//                        })
//                    }
//                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add Student Button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        // Validate Fields
                        fullNameError = fullName.isBlank()
                        rollNumberError = rollNumber.isBlank()
                        classError = selectedClass == "Select Class"
                        sectionError = section.isBlank()
                        Log.d("section",section)
                        if (!fullNameError && !rollNumberError && !classError && !sectionError) {
                            globalStateViewModel.addClassNameIfNotExists(selectedClass+"-"+section.uppercase())

                            studentViewModel.updateStudentId(System.currentTimeMillis().toString())
                            studentViewModel.updateFullName(fullName)
                            studentViewModel.updateRollNumber(rollNumber)
                            studentViewModel.updateClassName(newClass = selectedClass)
                            studentViewModel.updateSection(newSection=section.uppercase())
                            studentViewModel.updateSchoolId(sId = schoolId)

//                            onSelectedClassNameChange(selectedClass + "-" + section.uppercase())
                            studentViewModel.updateSelectedClassNameWithSection(selectedClass + "-" + section.uppercase())
//                            globalStateViewModel.updateIsRegistering()
//                            faceRecognitionViewModel.updateIsRegistering(true)
                              faceRecognitionViewModel.setRegistering(true)
//                            val dummyBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888).apply {
//                                eraseColor(0) // or Color.TRANSPARENT, Color.RED, etc.
//                            }
//                            faceRecognitionViewModel.registerFace(
//                                studentId = "STU123456",
//                                fullName = "Aryan Verma",
//                                rollNumber = "17",
//                                embedding = FloatArray(128) { it / 100f }, // Dummy embedding
//                                faceBitmap = dummyBitmap , // Replace with actual Bitmap captured from camera
//                                className = "11",
//                                section = "A",
//                                schoolId = "6818b58a427657d84b3c47ba",
//                                date = "03 Jun 2025"
//                            )
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

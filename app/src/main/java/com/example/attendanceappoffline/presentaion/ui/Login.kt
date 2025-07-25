import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendanceappoffline.common.LoginPreferenceManager
import com.example.attendanceappoffline.common.Result
import com.example.attendanceappoffline.common.Routes
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun LoginScreen(navController: NavController,authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val loginPrefs = remember { LoginPreferenceManager(context) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Pattern.compile(
            "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        )
        return emailRegex.matcher(email).matches()
    }
//    val loginResult by authViewModel.loginResult.collectAsState()
    val schoolDetails by authViewModel.schoolDetails.collectAsState()
    val loginResult by authViewModel.loginResult.collectAsState()

    LaunchedEffect(loginResult) {
        when (val result = loginResult) {
            is Result.Success -> {
                loginPrefs.setLoggedIn(true)
                Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                Log.d("reposne",result.data.data.user.schoolId)
                val schoolId=result.data.data.user.schoolId
                authViewModel.getSchoolDetails(schoolId)
                navController.navigate(Routes.HomePage) {
                    popUpTo(Routes.LoginPage) { inclusive = true }
                }
            }
            is Result.Error -> {
                Toast.makeText(context, "Login failed: ${result.message}", Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                // Optionally show loading UI

            }
            null -> {}
        }
    }

//    Log.d("loginResult2",loginResult.toString())
    Log.d("schoolDetails",schoolDetails.toString())
//    LaunchedEffect(loginResult) {
//        when (loginResult) {
//            is com.example.attendanceappoffline.common.Result.Success -> {
//                val data = (loginResult as com.example.attendanceappoffline.common.Result.Success).data
//                // Navigate to home screen or show success message
//                Log.d("reposne",data.data.user.schoolId)
//                val schoolId=data.data.user.schoolId
//                authViewModel.getSchoolDetails(schoolId)
//                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
//
//            }
//            is com.example.attendanceappoffline.common.Result.Error -> {
//                val message = (loginResult as Result.Error).message
//                // Show error message using Snackbar or Toast
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//
//            }
//            null -> {
//                // No result yet
//                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
//
//            }
//            com.example.attendanceappoffline.common.Result.Loading -> {
//
//            }
////            is com.example.attendanceappoffline.common.Result.Error -> TODO()
//        }
//    }
//    Log.d("loginDetail",loginResult.toString())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to PI Attendance",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    )
                )

                Button(
                    onClick = {
                        when {
                            email.isBlank() -> Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show()
                            !isValidEmail(email) -> Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                            password.isBlank() -> Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                            else -> {
                                authViewModel.login(email, password)
                            }
//                            email.isBlank() -> Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show()
//                            !isValidEmail(email) -> Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
//                            password.isBlank() -> Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
//                            else -> {
//                                scope.launch(Dispatchers.IO) {
//                                    authViewModel.login(email,password)
//                                    loginPrefs.setLoggedIn(true)
//                                }
//                                Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
//                                navController.navigate(Routes.HomePage) {
//                                    popUpTo(Routes.LoginPage) { inclusive = true }
//                                }
//                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Login")
                }
            }
        }
    }
}

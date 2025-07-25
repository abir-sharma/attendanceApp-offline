package com.example.attendanceappoffline.presentaion.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendanceappoffline.common.LoginPreferenceManager
import com.example.attendanceappoffline.common.Result
import com.example.attendanceappoffline.common.dataStore
import com.example.attendanceappoffline.data.models.LoginResponse
import com.example.attendanceappoffline.data.source.local.entity.SchoolClassEntity
import com.example.attendanceappoffline.domain.models.SchoolClass
import com.example.attendanceappoffline.domain.models.SchoolResponse
import com.example.attendanceappoffline.domain.usecases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val loginPrefs: LoginPreferenceManager
):ViewModel() {

    init {
//        viewModelScope.launch {
//            loginPrefs.schoolId.collect { id ->
//                _schoolId.value = id
//            }
//        }
    }

    private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginResult: StateFlow<Result<LoginResponse>?> = _loginResult

    private val _schoolDetails = MutableStateFlow<Result<SchoolResponse>?>(null)
    val schoolDetails: StateFlow<Result<SchoolResponse>?> = _schoolDetails

    private val _classNames = MutableStateFlow<List<String>>(emptyList())
    val classNames: StateFlow<List<String>> = _classNames


//    private val _schoolId = MutableStateFlow("")
//    val schoolId: StateFlow<String> = _schoolId
    private val _schoolId = MutableStateFlow("vew")
    val schoolId: StateFlow<String> = _schoolId




    suspend fun extractClassNames() {
        val result = schoolDetails.value
        if (result is Result.Success) {
            val names = result.data.classes?.mapNotNull { it.name } ?: emptyList()
            saveClassNamesLocally(names)
            val schoolId = result.data.id ?: return
            viewModelScope.launch {
                loginPrefs.saveSchoolId(schoolId)
            }
            loadSchoolId(loginPrefs)

        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            val result = authUseCase.login(email, password)
            _loginResult.value = result
            Log.d("loginResult", result.toString())
        }
    }

    fun loadSchoolId(loginPrefs: LoginPreferenceManager) {
        viewModelScope.launch {
            loginPrefs.schoolId.collect { id ->
                _schoolId.value = id
            }
        }
    }


    fun getSchoolDetails(schoolId: String) {
        viewModelScope.launch {
            val result = authUseCase.getSchoolDetails(schoolId)
            _schoolDetails.value = result
            extractClassNames()
            Log.d("sd", result.toString())
            Log.d("classes", classNames.value.toString())
        }
    }

    fun saveClassNamesLocally(classList: List<String>) {
        viewModelScope.launch {
            val entities = classList.map { SchoolClassEntity(name = it) }
            authUseCase.saveClassesLocally(entities)
            getClassesLocally()
        }
    }

    fun getClassesLocally() {
        viewModelScope.launch {
//            if (authUseCase.getAllClassLocal().isEmpty()) {
//                authUseCase.saveClassesLocally(emptyList()) // Or add default classes
//                Log.d("classes1","classes1")
//            }
            val classes=authUseCase.getAllClassLocal()
            Log.d("classes2",classes.toString())
            val classNames: List<String> = classes.map { it.name }
            _classNames.value=classNames
            Log.d("localClasses",classNames.toString())
        }
    }

}
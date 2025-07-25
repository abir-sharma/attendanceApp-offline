package com.example.attendanceappoffline.common

// LoginPreferenceManager.kt

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("login_prefs")
val Context.schoolDataStore by preferencesDataStore(name = "school_prefs")

class LoginPreferenceManager(private val context: Context) {
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val SCHOOL_ID_KEY = stringPreferencesKey("school_id")
    }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = value
        }
    }

    suspend fun saveSchoolId(schoolId: String) {
        context.schoolDataStore.edit { preferences ->
            preferences[SCHOOL_ID_KEY] = schoolId
        }
    }

    val schoolId: Flow<String> = context.schoolDataStore.data
        .map { prefs -> prefs[SCHOOL_ID_KEY]?:""}

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[IS_LOGGED_IN] ?: false }

}

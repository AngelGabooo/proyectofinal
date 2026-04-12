package com.angel.proyectofinal.features.auth.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val routineDao: RoutineDao
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val coachEmail = "coach@fitpro.com"

    fun onEmailChange(newValue: String) {
        email = newValue
        errorMessage = null
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        errorMessage = null
    }

    fun login(onResult: (Boolean) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa tu correo y contraseña"
            return
        }

        isLoading = true
        auth.signInWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        routineDao.resetAllRoutinesStatus()
                        routineDao.clearWorkoutHistory()

                        isLoading = false
                        val isCoach = email.trim().lowercase() == coachEmail.lowercase()
                        onResult(isCoach)
                    }
                } else {
                    isLoading = false
                    errorMessage = "Correo o contraseña incorrectos"
                }
            }
    }
}
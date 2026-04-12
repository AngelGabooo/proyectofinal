package com.angel.proyectofinal.features.auth.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val routineDao: RoutineDao
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun registerUser(onSuccess: () -> Unit) {
        // 1. Validar campos vacíos
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Todos los campos son obligatorios"
            return
        }

        // 2. Validar correo del Coach (No permitido para alumnos)
        if (email.trim().lowercase() == "coach@fitpro.com") {
            errorMessage = "Este correo está reservado para el STAFF"
            return
        }

        // 3. Validar contraseñas iguales
        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        // 4. Recomendación de contraseña segura (Requisito de Firebase + Extra)
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$".toRegex()
        if (!password.matches(passwordRegex)) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres, incluir una letra y un número"
            return
        }

        isLoading = true
        errorMessage = null

        auth.createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = fullName
                    }

                    task.result?.user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener {
                            viewModelScope.launch {
                                routineDao.resetAllRoutinesStatus()
                                routineDao.clearWorkoutHistory()
                                isLoading = false
                                onSuccess()
                            }
                        }
                } else {
                    isLoading = false
                    errorMessage = task.exception?.localizedMessage ?: "Error al registrar cuenta"
                }
            }
    }
}
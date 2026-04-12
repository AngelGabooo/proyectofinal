package com.angel.proyectofinal.features.routines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.WorkoutHistoryEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val routineDao: RoutineDao
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Canal para enviar eventos de mensajes a la UI
    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun finishWorkout(routineId: Int, routineName: String, steps: Int) {
        val timestamp = System.currentTimeMillis()
        val user = auth.currentUser
        val userId = user?.uid ?: "anonimo"
        val userEmail = user?.email ?: "desconocido"
        val userName = user?.displayName ?: userEmail.split("@")[0]

        viewModelScope.launch {
            // 1. SIEMPRE GUARDAMOS LOCAL (Offline-First)
            try {
                routineDao.insertHistory(
                    WorkoutHistoryEntity(
                        routineName = routineName,
                        stepsCompleted = steps,
                        dateMillis = timestamp
                    )
                )
                routineDao.updateRoutineStatus(routineId, "COMPLETADO")
            } catch (e: Exception) {
                _uiEvent.send("Error al guardar localmente")
            }

            // 2. INTENTAR SUBIR A LA NUBE
            val workoutData = hashMapOf(
                "userId" to userId,
                "userName" to userName,
                "userEmail" to userEmail,
                "routineName" to routineName,
                "steps" to steps,
                "timestamp" to timestamp
            )

            firestore.collection("alumnos_progreso")
                .add(workoutData)
                .addOnSuccessListener {
                    viewModelScope.launch { _uiEvent.send("¡Entrenamiento sincronizado con éxito! 💪") }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _uiEvent.send("Guardado local. Se sincronizará al conectar a internet. 📶")
                    }
                }
        }
    }
}
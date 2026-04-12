package com.angel.proyectofinal.features.coach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.RoutineEntity
import com.google.firebase.firestore.FirebaseFirestore // NUEVO
import com.google.firebase.firestore.Query // NUEVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow // NUEVO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow // NUEVO
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val routineDao: RoutineDao
) : ViewModel() {

    // --- TUS FUNCIONES ORIGINALES DE GESTIÓN DE RUTINAS (INTACTAS) ---

    val allRoutines = routineDao.getAllRoutinesForCoach()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveRoutine(name: String, day: String, objective: String, exercises: List<String>, restTime: String) {
        viewModelScope.launch {
            try {
                val time = restTime.trim().toIntOrNull() ?: 90
                val entity = RoutineEntity(
                    id = 0,
                    name = name,
                    dayOfWeek = day,
                    objective = objective,
                    description = "Rutina enfocada en $objective",
                    exercises = exercises.joinToString("\n"),
                    imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=500",
                    restTime = time,
                    isVisible = true,
                    isStepCounterActive = true
                )
                routineDao.insertRoutine(entity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteRoutine(routine: RoutineEntity) {
        viewModelScope.launch { routineDao.deleteRoutine(routine) }
    }

    fun toggleVisibility(routine: RoutineEntity) {
        viewModelScope.launch {
            routineDao.updateRoutine(routine.copy(isVisible = !routine.isVisible))
        }
    }

    fun toggleStepCounter(routine: RoutineEntity) {
        viewModelScope.launch {
            routineDao.updateRoutine(routine.copy(isStepCounterActive = !routine.isStepCounterActive))
        }
    }
    // Agrega esta función al final de tu CoachViewModel.kt
    fun deleteStudentProgress(timestamp: Long) {
        viewModelScope.launch {
            firestore.collection("alumnos_progreso")
                .whereEqualTo("timestamp", timestamp)
                .get()
                .addOnSuccessListener { snapshot ->
                    for (document in snapshot.documents) {
                        document.reference.delete()
                    }
                }
        }
    }
    // --- NUEVA SECCIÓN: LECTURA DE AVANCES DE ALUMNOS DESDE FIRESTORE ---

    private val firestore = FirebaseFirestore.getInstance()
    private val _studentsProgress = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val studentsProgress = _studentsProgress.asStateFlow()

    init {
        // Al iniciar el ViewModel, empezamos a escuchar los avances en la nube
        fetchStudentsProgress()
    }

    private fun fetchStudentsProgress() {
        firestore.collection("alumnos_progreso")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                val progressList = snapshot?.documents?.map { doc ->
                    doc.data ?: emptyMap()
                } ?: emptyList()

                _studentsProgress.value = progressList
            }
    }
}
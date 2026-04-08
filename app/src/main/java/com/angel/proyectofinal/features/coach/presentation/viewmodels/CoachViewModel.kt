package com.angel.proyectofinal.features.coach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.RoutineEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val routineDao: RoutineDao
) : ViewModel() {

    val allRoutines = routineDao.getAllRoutinesForCoach()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveRoutine(name: String, day: String, objective: String, exercises: List<String>, restTime: String) {
        viewModelScope.launch {
            try {
                // Protección contra errores de escritura: convertimos a Int con seguridad
                val time = restTime.trim().toIntOrNull() ?: 90

                val entity = RoutineEntity(
                    id = 0, // 0 asegura que Room genere un nuevo ID (Auto-increment)
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
                e.printStackTrace() // Esto evita que el crash cierre la app y lo imprime en Logcat
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
}
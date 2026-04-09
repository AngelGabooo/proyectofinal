package com.angel.proyectofinal.features.routines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.RoutineEntity
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.angel.proyectofinal.features.routines.domain.usecases.GetRoutinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutinesViewModel @Inject constructor(
    private val getRoutinesUseCase: GetRoutinesUseCase,
    private val routineDao: RoutineDao
) : ViewModel() {

    val routines = getRoutinesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            val currentRoutines = routineDao.getAllRoutinesForCoach().first()
            if (currentRoutines.isEmpty()) {
                // En tu RoutinesViewModel.kt, actualiza la parte de initialRoutines:

                val initialRoutines = listOf(
                    RoutineEntity(
                        id = 1,
                        name = "Día de Pecho",
                        dayOfWeek = "Lunes",
                        objective = "Fuerza",
                        description = "Enfocado en press y aperturas",
                        exercises = "Banca: 3x12, Inclinado: 4x10",
                        status = "Pendiente",
                        imageUrl = "https://images.pexels.com/photos/416809/pexels-photo-416809.jpeg?auto=compress&cs=tinysrgb&w=500",
                        isVisible = true,
                        isStepCounterActive = true,
                        restTime = 90
                    ),
                    RoutineEntity(
                        id = 2,
                        name = "Día de Pierna",
                        dayOfWeek = "Martes",
                        objective = "Volumen",
                        description = "Sentadillas y prensa",
                        exercises = "Sentadilla: 4x10, Prensa: 3x12",
                        status = "Pendiente",
                        imageUrl = "https://images.pexels.com/photos/4761799/pexels-photo-4761799.jpeg?auto=compress&cs=tinysrgb&w=500",
                        isVisible = true,
                        isStepCounterActive = true,
                        restTime = 120
                    ),
                    RoutineEntity(
                        id = 3,
                        name = "Día de Espalda",
                        dayOfWeek = "Miércoles",
                        objective = "Fuerza",
                        description = "Dominadas y remo",
                        exercises = "Dominadas: 3x12, Remo: 4x8",
                        status = "Pendiente",
                        imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg?auto=compress&cs=tinysrgb&w=500",
                        isVisible = true,
                        isStepCounterActive = false,
                        restTime = 90
                    ),
                    // Puedes agregar más rutinas con estas imágenes:
                    RoutineEntity(
                        id = 4,
                        name = "Día de Hombros",
                        dayOfWeek = "Jueves",
                        objective = "Definición",
                        description = "Press militar y elevaciones",
                        exercises = "Press Militar: 4x10, Elevaciones: 3x12",
                        status = "Pendiente",
                        imageUrl = "https://images.pexels.com/photos/416778/pexels-photo-416778.jpeg?auto=compress&cs=tinysrgb&w=500",
                        isVisible = true,
                        isStepCounterActive = true,
                        restTime = 90
                    ),
                    RoutineEntity(
                        id = 5,
                        name = "Día de Brazos",
                        dayOfWeek = "Viernes",
                        objective = "Volumen",
                        description = "Bíceps y tríceps",
                        exercises = "Curl: 3x12, Tríceps: 4x10",
                        status = "Pendiente",
                        imageUrl = "https://images.pexels.com/photos/1825440/pexels-photo-1825440.jpeg?auto=compress&cs=tinysrgb&w=500",
                        isVisible = true,
                        isStepCounterActive = true,
                        restTime = 90
                    ),
                    RoutineEntity(
                        id = 6,
                        name = "Cardio HIIT",
                        dayOfWeek = "Sábado",
                        objective = "Resistencia",
                        description = "Entrenamiento cardiovascular",
                        exercises = "Burpees: 4x15, Saltos: 3x20",
                        status = "Pendiente",
                        imageUrl = "https://images.pexels.com/photos/1954521/pexels-photo-1954521.jpeg?auto=compress&cs=tinysrgb&w=500",
                        isVisible = true,
                        isStepCounterActive = true,
                        restTime = 60
                    )
                )
                initialRoutines.forEach { routineDao.insertRoutine(it) }
            }
        }
    }
}
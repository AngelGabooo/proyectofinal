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

    // SharingStarted.WhileSubscribed(5000) permite que el flujo se mantenga vivo
    // 5 segundos después de salir de la pantalla, ahorrando recursos.
    val routines = getRoutinesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            // Verificamos si la base de datos está vacía para insertar datos de prueba
            val currentRoutines = routineDao.getAllRoutinesForCoach().first()
            if (currentRoutines.isEmpty()) {
                val initialRoutines = listOf(
                    RoutineEntity(
                        id = 1,
                        name = "Día de Pecho",
                        dayOfWeek = "Lunes",
                        objective = "Fuerza",
                        description = "Press banca, flies, fondos",
                        exercises = "Banca: 3x12, Inclinado: 4x10",
                        status = "Pendiente",
                        imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=500",
                        isVisible = true,
                        isStepCounterActive = true, // NUEVO: Obligatorio
                        restTime = 90               // NUEVO: Obligatorio
                    ),
                    RoutineEntity(
                        id = 2,
                        name = "Día de Pierna",
                        dayOfWeek = "Martes",
                        objective = "Volumen",
                        description = "Sentadillas, leg press, curl femoral",
                        exercises = "Sentadilla: 4x10, Prensa: 3x12",
                        status = "Pendiente",
                        imageUrl = "https://images.unsplash.com/photo-1574680096145-d05b474e2155?q=80&w=500",
                        isVisible = true,
                        isStepCounterActive = true, // NUEVO
                        restTime = 120              // NUEVO (Más descanso para pierna)
                    ),
                    RoutineEntity(
                        id = 3,
                        name = "Día de Espalda",
                        dayOfWeek = "Miércoles",
                        objective = "Fuerza",
                        description = "Dominadas, remo con barra, pullover",
                        exercises = "Dominadas: 3x12, Remo: 4x8",
                        status = "Pendiente",
                        imageUrl = "https://images.unsplash.com/photo-1603287611837-517b1633598e?q=80&w=500",
                        isVisible = true,
                        isStepCounterActive = false, // EJEMPLO: Desactivado para espalda
                        restTime = 90                // NUEVO
                    )
                )
                initialRoutines.forEach { routineDao.insertRoutine(it) }
            }
        }
    }
}
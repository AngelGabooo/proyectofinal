package com.angel.proyectofinal.features.coach.domain.usecases

import com.angel.proyectofinal.features.routines.domain.entities.Routine
import com.angel.proyectofinal.features.routines.domain.repositories.RoutineRepository
import javax.inject.Inject

class CreateRoutineUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(routine: Routine) {
        repository.createRoutine(routine)
    }
}
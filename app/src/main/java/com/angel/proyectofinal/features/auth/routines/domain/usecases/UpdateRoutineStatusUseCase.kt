package com.angel.proyectofinal.features.routines.domain.usecases

import com.angel.proyectofinal.features.routines.domain.repositories.RoutineRepository
import javax.inject.Inject

class UpdateRoutineStatusUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(routineId: Int, status: String) =
        repository.updateRoutineStatus(routineId, status)
}
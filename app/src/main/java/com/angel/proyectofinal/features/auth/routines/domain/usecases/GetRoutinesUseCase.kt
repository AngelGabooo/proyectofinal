package com.angel.proyectofinal.features.routines.domain.usecases

import com.angel.proyectofinal.features.routines.domain.entities.Routine
import com.angel.proyectofinal.features.routines.domain.repositories.RoutineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoutinesUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    operator fun invoke(): Flow<List<Routine>> = repository.getAllRoutines()
}
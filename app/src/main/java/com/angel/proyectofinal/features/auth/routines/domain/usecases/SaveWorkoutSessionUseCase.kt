package com.angel.proyectofinal.features.routines.domain.usecases

import com.angel.proyectofinal.features.routines.domain.entities.WorkoutSession
import com.angel.proyectofinal.features.routines.domain.repositories.RoutineRepository
import javax.inject.Inject

class SaveWorkoutSessionUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(session: WorkoutSession) =
        repository.saveWorkoutSession(session)
}
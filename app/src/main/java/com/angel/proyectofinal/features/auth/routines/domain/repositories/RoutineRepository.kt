package com.angel.proyectofinal.features.routines.domain.repositories

import com.angel.proyectofinal.features.routines.domain.entities.Routine
import com.angel.proyectofinal.features.routines.domain.entities.WorkoutSession
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getAllRoutines(): Flow<List<Routine>>
    suspend fun updateRoutineStatus(routineId: Int, status: String)
    fun getWorkoutSession(routineId: Int): Flow<WorkoutSession?>
    suspend fun saveWorkoutSession(session: WorkoutSession)
    suspend fun createRoutine(routine: Routine)
}
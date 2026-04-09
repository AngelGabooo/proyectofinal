package com.angel.proyectofinal.features.routines.data.repositories

import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.RoutineEntity
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.WorkoutSessionEntity
import com.angel.proyectofinal.features.routines.domain.entities.Routine
import com.angel.proyectofinal.features.routines.domain.entities.WorkoutSession
import com.angel.proyectofinal.features.routines.domain.repositories.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao
) : RoutineRepository {

    override fun getAllRoutines(): Flow<List<Routine>> =
        routineDao.getAllRoutines().map { list ->
            list.map { entity ->
                Routine(
                    id = entity.id,
                    name = entity.name ?: "Sin nombre",
                    dayOfWeek = entity.dayOfWeek ?: "",
                    objective = entity.objective ?: "",
                    description = entity.description ?: "",
                    exercises = entity.exercises ?: "",
                    status = entity.status ?: "Pendiente",
                    imageUrl = entity.imageUrl ?: "", // Aseguramos que no sea nulo
                    isVisible = entity.isVisible,
                    isStepCounterActive = entity.isStepCounterActive,
                    restTime = entity.restTime
                )
            }
        }

    override suspend fun updateRoutineStatus(routineId: Int, status: String) {
        val entity = routineDao.getRoutineById(routineId).first()
        routineDao.updateRoutine(entity.copy(status = status))
    }

    override fun getWorkoutSession(routineId: Int): Flow<WorkoutSession?> =
        routineDao.getWorkoutSession(routineId).map { it?.let { s ->
            WorkoutSession(s.routineId, s.currentExerciseIndex, s.remainingTimeSeconds, s.isActive)
        }}

    override suspend fun saveWorkoutSession(session: WorkoutSession) {
        routineDao.saveWorkoutSession(
            WorkoutSessionEntity(session.routineId, session.currentExerciseIndex, session.remainingTimeSeconds, session.isActive)
        )
    }

    override suspend fun createRoutine(routine: Routine) {
        routineDao.insertRoutine(
            RoutineEntity(
                id = routine.id,
                name = routine.name,
                dayOfWeek = routine.dayOfWeek,
                objective = routine.objective,
                description = routine.description,
                exercises = routine.exercises,
                status = routine.status,
                imageUrl = routine.imageUrl,
                isVisible = routine.isVisible,
                isStepCounterActive = routine.isStepCounterActive, // Faltaba este?
                restTime = routine.restTime                        // Faltaba este?
            )
        )
    }
}
package com.angel.proyectofinal.features.routines.data.datasources.local.dao

import androidx.room.*
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.RoutineEntity
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines WHERE isVisible = 1")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines")
    fun getAllRoutinesForCoach(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE id = :id")
    fun getRoutineById(id: Int): Flow<RoutineEntity>

    @Update
    suspend fun updateRoutine(routine: RoutineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity)

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    @Query("SELECT * FROM workout_session WHERE routineId = :routineId")
    fun getWorkoutSession(routineId: Int): Flow<WorkoutSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorkoutSession(session: WorkoutSessionEntity)
}
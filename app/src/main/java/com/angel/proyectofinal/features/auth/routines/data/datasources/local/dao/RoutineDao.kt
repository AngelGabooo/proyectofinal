package com.angel.proyectofinal.features.routines.data.datasources.local.dao

import androidx.room.*
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.RoutineEntity
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.WorkoutSessionEntity
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.WorkoutHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    // --- GESTIÓN DE RUTINAS ---
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

    // --- SESIONES DE ENTRENAMIENTO ---
    @Query("SELECT * FROM workout_session WHERE routineId = :routineId")
    fun getWorkoutSession(routineId: Int): Flow<WorkoutSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorkoutSession(session: WorkoutSessionEntity)

    // --- HISTORIAL Y ESTADÍSTICAS ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: WorkoutHistoryEntity)

    @Query("UPDATE routines SET status = :status WHERE id = :routineId")
    suspend fun updateRoutineStatus(routineId: Int, status: String)

    @Query("SELECT COUNT(*) FROM workout_history")
    fun getTotalWorkouts(): Flow<Int>

    // --- FUNCIONES DE LIMPIEZA PARA NUEVOS USUARIOS ---

    // 1. Pone todas las rutinas en 'Pendiente'
    @Query("UPDATE routines SET status = 'Pendiente'")
    suspend fun resetAllRoutinesStatus()

    // 2. Borra todo el historial de entrenamientos (Limpia Estadísticas/Racha)
    @Query("DELETE FROM workout_history")
    suspend fun clearWorkoutHistory()

    // 3. Borra sesiones guardadas (Limpia cronómetros a medias)
    @Query("DELETE FROM workout_session")
    suspend fun clearAllWorkoutSessions()

    // NOTA: Si tienes una tabla específica para podómetro llamada 'step_logs',
    // deberías añadir: @Query("DELETE FROM step_logs") suspend fun clearStepLogs()
}
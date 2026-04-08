package com.angel.proyectofinal.features.routines.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_session")
data class WorkoutSessionEntity(
    @PrimaryKey val routineId: Int,
    var currentExerciseIndex: Int = 0,
    var remainingTimeSeconds: Int = 90,
    var isActive: Boolean = false
)
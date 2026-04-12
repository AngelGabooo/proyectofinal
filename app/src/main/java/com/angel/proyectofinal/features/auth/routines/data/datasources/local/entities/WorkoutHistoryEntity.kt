package com.angel.proyectofinal.features.routines.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routineName: String,
    val dateMillis: Long = System.currentTimeMillis(),
    val stepsCompleted: Int
)
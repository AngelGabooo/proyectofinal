package com.angel.proyectofinal.features.routines.domain.entities

data class WorkoutSession(
    val routineId: Int,
    val currentExerciseIndex: Int,
    val remainingTimeSeconds: Int,
    val isActive: Boolean
)
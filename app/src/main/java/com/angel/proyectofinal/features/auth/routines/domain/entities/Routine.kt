package com.angel.proyectofinal.features.routines.domain.entities

data class Routine(
    val id: Int,
    val name: String,
    val dayOfWeek: String,
    val objective: String,
    val description: String,
    val exercises: String,
    val status: String,
    val imageUrl: String,
    val isVisible: Boolean,
    val isStepCounterActive: Boolean,
    val restTime: Int
)
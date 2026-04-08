package com.angel.proyectofinal.features.routines.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val dayOfWeek: String = "",
    val objective: String = "",
    val description: String = "",
    val exercises: String = "",
    val status: String = "Pendiente",
    val imageUrl: String = "",
    val isVisible: Boolean = true,
    val isStepCounterActive: Boolean = true,
    val restTime: Int = 90,
    var lastUpdated: Long = System.currentTimeMillis()
)
package com.angel.proyectofinal.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routineDao: RoutineDao
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // Obtenemos el nombre del usuario desde Firebase.
    // Si no tiene nombre (displayName), usamos la primera parte del email.
    val userName: String
        get() = auth.currentUser?.displayName ?: auth.currentUser?.email?.split("@")?.get(0) ?: "USUARIO"

    val completedWorkouts = routineDao.getTotalWorkouts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
}
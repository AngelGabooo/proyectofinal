package com.angel.proyectofinal.features.routines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.proyectofinal.features.routines.domain.entities.WorkoutSession
import com.angel.proyectofinal.features.routines.domain.usecases.SaveWorkoutSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val saveWorkoutSessionUseCase: SaveWorkoutSessionUseCase
) : ViewModel() {

    private val _session = MutableStateFlow<WorkoutSession?>(null)
    val session = _session.asStateFlow()

    fun updateTime(remaining: Int) {
        _session.value?.let { current ->
            viewModelScope.launch {
                val updated = current.copy(remainingTimeSeconds = remaining)
                _session.value = updated
                saveWorkoutSessionUseCase(updated)
            }
        }
    }

    fun setSession(session: WorkoutSession) {
        _session.value = session
    }
}
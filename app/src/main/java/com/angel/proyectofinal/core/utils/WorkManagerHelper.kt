package com.angel.proyectofinal.core.utils

import android.content.Context
import androidx.work.*
import com.angel.proyectofinal.core.workers.ReminderWorker
import java.util.concurrent.TimeUnit

class WorkManagerHelper(private val context: Context) {

    fun scheduleDailyReminder() {
        // Definimos una tarea de UNA SOLA VEZ que se ejecute 10 minutos después
        val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(10, TimeUnit.MINUTES) // CAMBIO: Ahora espera 10 minutos
            .addTag("reminder_worker")
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true) // Estrategia: Solo si tiene batería
                    .build()
            )
            .build()

        // Usamos enqueueUniqueWork con REPLACE para que, si el usuario abre y cierra
        // la app varias veces, el contador de 10 minutos se reinicie desde el último cierre.
        WorkManager.getInstance(context).enqueueUniqueWork(
            "WorkoutReminderTask",
            ExistingWorkPolicy.REPLACE,
            reminderRequest
        )
    }
}
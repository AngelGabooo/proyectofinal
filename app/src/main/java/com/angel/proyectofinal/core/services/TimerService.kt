package com.angel.proyectofinal.core.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.angel.proyectofinal.core.utils.HapticHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerService : Service() {

    private val binder = TimerBinder()
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var timerJob: Job? = null

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft = _timeLeft.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private lateinit var notificationHelper: TimerNotificationHelper
    private lateinit var hapticHelper: HapticHelper

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        notificationHelper = TimerNotificationHelper(this)
        hapticHelper = HapticHelper(this)
        notificationHelper.createChannel()
    }

    fun startTimer(seconds: Int) {
        if (_isRunning.value) return

        _timeLeft.value = seconds
        _isRunning.value = true

        val notification = notificationHelper.createNotification(formatTime(seconds))
        startForeground(TimerNotificationHelper.NOTIFICATION_ID, notification)

        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value -= 1

                if (_timeLeft.value > 0) {
                    updateNotification(formatTime(_timeLeft.value))
                }
            }
            // Ejecutar al llegar a 0
            handleTimerFinished()
        }
    }

    private fun handleTimerFinished() {
        _isRunning.value = false

        // 1. Vibración fuerte (Hardware 4)
        hapticHelper.triggerSuccess()

        // 2. Notificación de fin con un ID DIFERENTE para que Android no la ignore
        val finalNotification = notificationHelper.createNotification("¡TIEMPO AGOTADO! Dale a la siguiente serie 💪")
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Usamos un ID distinto (ej. 102) para que aparezca como una notificación nueva
        notificationManager.notify(102, finalNotification)

        // 3. Quitamos el servicio del primer plano y eliminamos la del cronómetro (ID 101)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun stopTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun updateNotification(timeText: String) {
        val notification = notificationHelper.createNotification(timeText)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(TimerNotificationHelper.NOTIFICATION_ID, notification)
    }

    private fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format("%02d:%02d", m, s)
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}
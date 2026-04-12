package com.angel.proyectofinal.core.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.angel.proyectofinal.MainActivity

class TimerNotificationHelper(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "timer_channel_v2"
        const val NOTIFICATION_ID = 101
    }

    fun createNotification(timeLeft: String): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = Settings.System.DEFAULT_NOTIFICATION_URI

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Descanso en curso - FitPro UP")
            .setContentText("Tiempo restante: $timeLeft")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
    }

    fun createChannel() {
        val manager = context.getSystemService(NotificationManager::class.java)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
            .build()

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Alertas FitPro UP",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Canal para cronómetro de descanso y recordatorios"
            enableLights(true)
            enableVibration(true)
            setSound(Settings.System.DEFAULT_NOTIFICATION_URI, audioAttributes)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(channel)
    }
}
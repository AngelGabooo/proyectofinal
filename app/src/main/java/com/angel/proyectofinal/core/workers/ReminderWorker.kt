package com.angel.proyectofinal.core.workers

import android.app.NotificationManager
import android.content.Context
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.angel.proyectofinal.core.services.TimerNotificationHelper

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val notificationHelper = TimerNotificationHelper(applicationContext)
        notificationHelper.createChannel()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = Settings.System.DEFAULT_NOTIFICATION_URI

        val notification = NotificationCompat.Builder(
            applicationContext,
            TimerNotificationHelper.CHANNEL_ID
        )
            .setContentTitle("🏆 ¡Ánimo Angel!")
            .setContentText("No rompas la racha. ¡Tu entrenamiento te espera!")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .build()

        notificationManager.notify(202, notification)
        return Result.success()
    }
}
package com.angel.proyectofinal.core.services

import android.app.NotificationManager
import android.content.Context
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Verificamos si el mensaje trae datos o notificación
        remoteMessage.notification?.let {
            showNotification(it.title ?: "FitPro UP", it.body ?: "Tienes un nuevo mensaje del Coach")
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val helper = TimerNotificationHelper(applicationContext)
        helper.createChannel()

        val notification = NotificationCompat.Builder(applicationContext, TimerNotificationHelper.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        notificationManager.notify(303, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Este token es la "dirección" única de este celular para el Coach
        println("Firebase Token: $token")
    }
}
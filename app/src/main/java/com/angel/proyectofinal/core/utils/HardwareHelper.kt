package com.angel.proyectofinal.core.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.MutableStateFlow

class HardwareHelper(private val context: Context) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private var stepListener: SensorEventListener? = null
    val stepCountFlow = MutableStateFlow(0)
    private var initialSteps = -1

    // Referencia para poder detener la alarma después
    private var currentRingtone: Ringtone? = null

    fun startStepCounter() {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) ?: return
        initialSteps = -1

        stepListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val sensorValue = event.values[0].toInt()
                if (initialSteps == -1) {
                    initialSteps = sensorValue
                }
                stepCountFlow.value = sensorValue - initialSteps
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(stepListener, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopStepCounter() {
        stepListener?.let { sensorManager.unregisterListener(it) }
    }

    fun playWorkoutEndAlert() {
        // Detener cualquier alarma previa antes de iniciar una nueva
        stopAlarm()

        // 1. Vibración intermitente
        val timings = longArrayOf(0, 500, 200, 500, 200, 500)
        val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
        vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))

        // 2. Sonido de Alarma Real
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

            currentRingtone = RingtoneManager.getRingtone(context, alarmUri)

            currentRingtone?.apply {
                audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- FUNCIÓN NUEVA PARA APAGAR LA ALARMA ---
    fun stopAlarm() {
        try {
            if (currentRingtone?.isPlaying == true) {
                currentRingtone?.stop()
            }
            vibrator.cancel() // También detiene la vibración si sigue activa
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun canUseBiometric(): Boolean {
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        return BiometricManager.from(context).canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun showBiometricPrompt(activity: FragmentActivity, onSuccess: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Seguridad FitPro UP")
            .setSubtitle("Usa tu huella, rostro o PIN")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        prompt.authenticate(promptInfo)
    }
}
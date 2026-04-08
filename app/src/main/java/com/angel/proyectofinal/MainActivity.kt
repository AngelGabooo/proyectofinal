package com.angel.proyectofinal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.angel.proyectofinal.navigation.AppNavHost
import com.angel.proyectofinal.ui.theme.ProyectofinalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Registramos el lanzador para pedir permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Aquí podrías manejar si el usuario aceptó o no
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- PETICIÓN DE PERMISOS PARA EL SENSOR ---
        checkAndRequestStepSensorPermission()

        enableEdgeToEdge()
        setContent {
            ProyectofinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Agregamos el padding para que no se encime con el sistema
                    AppNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun checkAndRequestStepSensorPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }
    }
}
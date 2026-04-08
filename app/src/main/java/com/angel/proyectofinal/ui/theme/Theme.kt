package com.angel.proyectofinal.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Paleta de colores para el modo oscuro (Gimnasio/Fitness)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00C853),      // Verde neón fitness
    secondary = Color(0xFF00B0FF),    // Azul deportivo
    tertiary = Color(0xFFFFD600),     // Amarillo vibrante (opcional)
    background = Color(0xFF0F0F0F),   // Negro profundo
    surface = Color(0xFF1E1E1E),      // Gris muy oscuro para tarjetas
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// Paleta de colores para el modo claro
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00C853),
    secondary = Color(0xFF00B0FF),
    tertiary = Color(0xFF009624),
    background = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun ProyectofinalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivamos dynamicColor por defecto para que siempre use tus colores de gimnasio
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
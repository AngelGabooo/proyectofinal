package com.angel.proyectofinal.features.stats.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.stats.presentation.viewmodels.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController, viewModel: StatsViewModel = hiltViewModel()) {
    // Datos de ejemplo (pueden venir del ViewModel después)
    val daysAttended = 18
    val totalDays = 30
    val routinesCompleted = 11
    val totalRoutines = 15

    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ESTADÍSTICAS",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TARJETA 1: PROGRESO CIRCULAR (OBJETIVO)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "OBJETIVO MENSUAL",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(24.dp))

                    CircularProgress(
                        progress = (daysAttended.toFloat() / totalDays),
                        text = "${(daysAttended * 100 / totalDays)}%",
                        color = Color(0xFF00C853)
                    )
                }
            }

            // TARJETA 2: GRÁFICAS DE BARRAS MODERNAS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "DETALLE DE ACTIVIDAD",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(24.dp))

                    ModernBarChart(
                        label = "Días asistidos",
                        current = daysAttended,
                        total = totalDays,
                        gradient = listOf(Color(0xFF00C853), Color(0xFFB2FF59))
                    )

                    Spacer(Modifier.height(24.dp))

                    ModernBarChart(
                        label = "Rutinas completadas",
                        current = routinesCompleted,
                        total = totalRoutines,
                        gradient = listOf(Color(0xFF00B0FF), Color(0xFF00E5FF))
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // BOTÓN DE RETORNO
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text(
                    "VOLVER AL PANEL",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ModernBarChart(label: String, current: Int, total: Int, gradient: List<Color>) {
    val progressTarget = current.toFloat() / total
    // Animación de entrada para la barra
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 1000),
        label = "BarProgress"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.White, fontWeight = FontWeight.Medium)
            Text("$current / $total", color = gradient[0], fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(10.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(16.dp)) {
            // Fondo (riel de la barra)
            drawRoundRect(
                color = Color(0xFF333333),
                size = size,
                cornerRadius = CornerRadius(100f)
            )
            // Progreso con gradiente
            drawRoundRect(
                brush = Brush.horizontalGradient(gradient),
                size = Size(width = size.width * animatedProgress, height = size.height),
                cornerRadius = CornerRadius(100f)
            )
        }
    }
}

@Composable
fun CircularProgress(progress: Float, text: String, color: Color) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1200),
        label = "CircularProgress"
    )

    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(200.dp)) {
            // Círculo base (fondo opaco)
            drawArc(
                color = color.copy(alpha = 0.1f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round)
            )
            // Círculo de progreso con gradiente de barrido
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(color.copy(alpha = 0.4f), color, color)
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                color = Color.White
            )
            Text(
                "COMPLETADO",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                letterSpacing = 1.sp
            )
        }
    }
}
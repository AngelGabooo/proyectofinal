package com.angel.proyectofinal.features.stats.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // Importante
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // Importante
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.stats.presentation.viewmodels.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController, viewModel: StatsViewModel = hiltViewModel()) {
    // Colores GymStyle
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)
    val gymSuccess = Color(0xFF00E676)

    // Datos de ejemplo
    val daysAttended = 18
    val totalDays = 30
    val routinesCompleted = 11
    val totalRoutines = 15
    val totalMinutes = 1240
    val caloriesBurned = 3850

    val attendancePercentage = (daysAttended * 100 / totalDays)
    val routinesPercentage = (routinesCompleted * 100 / totalRoutines)

    // Estado del Scroll
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = gymBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "ESTADÍSTICAS",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                fontSize = 20.sp
                            ),
                            color = gymWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = gymDarkGray,
                    scrolledContainerColor = gymBlack
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                // HABILITAMOS EL SCROLL AQUÍ
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TARJETA DE BIENVENIDA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = androidx.compose.foundation.BorderStroke(1.dp, gymBorderGray)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = gymAccent.copy(alpha = 0.15f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.EmojiEvents, null, tint = gymAccent, modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TU PROGRESO", color = gymLightGray, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
                        Text("¡Vas por buen camino!", color = gymWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("${attendancePercentage}%", color = gymAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            // TARJETA PROGRESO CIRCULAR
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = androidx.compose.foundation.BorderStroke(1.dp, gymBorderGray)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, null, tint = gymAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OBJETIVO MENSUAL", color = gymLightGray, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(20.dp))
                    CircularProgress(
                        progress = daysAttended.toFloat() / totalDays,
                        text = "$attendancePercentage%",
                        subtitle = "ASISTENCIA",
                        color = gymAccent,
                        secondaryText = "$daysAttended de $totalDays días"
                    )
                }
            }

            // MÉTRICAS CLAVE
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = androidx.compose.foundation.BorderStroke(1.dp, gymBorderGray)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Leaderboard, null, tint = gymAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("MÉTRICAS CLAVE", color = gymLightGray, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(20.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        KPICard("RUTINAS", "$routinesCompleted/$totalRoutines", routinesPercentage, Icons.Default.FitnessCenter, Color(0xFF00D4FF))
                        KPICard("MINUTOS", formatTime(totalMinutes), calculatePercentage(totalMinutes, 1800), Icons.Default.Timer, gymAccent)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        KPICard("CALORÍAS", formatNumber(caloriesBurned), calculatePercentage(caloriesBurned, 5000), Icons.Default.Whatshot, Color(0xFFFFB300))
                        KPICard("RACHA", "7", 70, Icons.Default.FlashOn, gymSuccess)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN DE VOLVER
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = gymCardGray, contentColor = gymWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, gymBorderGray)
            ) {
                Icon(Icons.Default.ArrowBack, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("VOLVER AL PANEL", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Espacio extra al final para que el scroll no quede pegado al borde
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- RESTO DE COMPONENTES SE MANTIENEN IGUAL ---

@Composable
fun KPICard(title: String, value: String, percentage: Int, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2C2E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = color.copy(alpha = 0.15f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color(0xFF8E8E93), fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("$percentage%", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RowScope.KPICard(title: String, value: String, percentage: Int, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    KPICard(title, value, percentage, icon, color, Modifier.weight(1f))
}

@Composable
fun CircularProgress(progress: Float, text: String, subtitle: String, color: Color, secondaryText: String) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1200),
        label = "CircularProgress"
    )

    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(160.dp)) {
            drawArc(
                color = color.copy(alpha = 0.1f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(colors = listOf(color.copy(alpha = 0.4f), color, color)),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = text, style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black, fontSize = 32.sp), color = color)
            Text(subtitle, color = Color(0xFF8E8E93), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(secondaryText, color = Color(0xFF8E8E93).copy(alpha = 0.7f), fontSize = 11.sp)
        }
    }
}

fun formatTime(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60
    return if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
}

fun formatNumber(number: Int): String {
    return if (number >= 1000) "${number / 1000}K" else number.toString()
}

fun calculatePercentage(current: Int, total: Int): Int {
    return if (total > 0) ((current.toFloat() / total) * 100).toInt().coerceIn(0, 100) else 0
}
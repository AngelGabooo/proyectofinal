package com.angel.proyectofinal.features.routines.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.angel.proyectofinal.core.utils.HardwareHelper
import com.angel.proyectofinal.features.routines.presentation.viewmodels.RoutinesViewModel
import com.angel.proyectofinal.features.routines.presentation.viewmodels.WorkoutViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    navController: NavController,
    routineId: Int,
    workoutViewModel: WorkoutViewModel = hiltViewModel(),
    routinesViewModel: RoutinesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val hardware = remember { HardwareHelper(context) }

    // Obtenemos las rutinas para buscar la actual
    val routines by routinesViewModel.routines.collectAsState()
    val currentRoutine = remember(routines) { routines.find { it.id == routineId } }

    // MODIFICACIÓN: El tiempo inicial ahora depende de lo que el Coach configuró
    var timeLeft by remember(currentRoutine) {
        mutableIntStateOf(currentRoutine?.restTime ?: 90)
    }

    var isRunning by remember { mutableStateOf(false) }
    val steps by hardware.stepCountFlow.collectAsState()

    LaunchedEffect(Unit) {
        hardware.startStepCounter()
    }

    DisposableEffect(Unit) {
        onDispose {
            hardware.stopStepCounter()
            hardware.stopAlarm()
        }
    }

    LaunchedEffect(isRunning) {
        if (isRunning && timeLeft > 0) {
            while (timeLeft > 0 && isRunning) {
                delay(1000L)
                timeLeft--
            }
            if (timeLeft == 0) {
                hardware.playWorkoutEndAlert()
                isRunning = false
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        topBar = {
            TopAppBar(
                title = { Text("GUÍA DE RUTINA", fontWeight = FontWeight.Black, color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E1E1E))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- SECCIÓN DE LA IMAGEN ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                if (currentRoutine?.imageUrl != null) {
                    AsyncImage(
                        model = currentRoutine.imageUrl,
                        contentDescription = "Ejemplo de ejercicio",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00C853))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- CRONÓMETRO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("DESCANSO RESTANTE", color = Color(0xFF00C853), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(
                        text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Black
                        ),
                        color = if (timeLeft < 10) Color(0xFFFF5252) else White
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // --- SECCIÓN INFORMATIVA (Pasos y Sensado) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // MODIFICACIÓN: Solo mostramos la Card de pasos si el Coach lo activó
                if (currentRoutine?.isStepCounterActive == true) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("PASOS", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                            Text("$steps", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = White)
                        }
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SENSADO", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                        Text("ON", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color(0xFF00C853))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- BOTONES DE CONTROL ---
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        if (isRunning) isRunning = false
                        else {
                            hardware.stopAlarm()
                            isRunning = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) Color(0xFF444444) else Color(0xFF00C853)
                    )
                ) {
                    Text(
                        if (isRunning) "PAUSAR" else "INICIAR DESCANSO",
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isRunning) White else Black
                    )
                }

                OutlinedButton(
                    onClick = {
                        hardware.stopAlarm()
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFFF5252)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252))
                ) {
                    Text("FINALIZAR", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
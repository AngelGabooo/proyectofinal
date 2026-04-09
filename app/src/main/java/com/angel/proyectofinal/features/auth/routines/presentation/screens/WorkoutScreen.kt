package com.angel.proyectofinal.features.routines.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
    // Colores GymStyle
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)
    val gymSuccess = Color(0xFF00E676)
    val gymWarning = Color(0xFFFFB300)

    val context = LocalContext.current
    val hardware = remember { HardwareHelper(context) }
    val routines by routinesViewModel.routines.collectAsState()

    val currentRoutine = remember(routines, routineId) {
        routines.find { it.id == routineId }
    }

    var timeLeft by remember(currentRoutine) {
        mutableIntStateOf(currentRoutine?.restTime ?: 90)
    }
    var isRunning by remember { mutableStateOf(false) }
    val steps by hardware.stepCountFlow.collectAsState()

    LaunchedEffect(Unit) { hardware.startStepCounter() }
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
        containerColor = gymBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "MODO ENTRENAMIENTO",
                            fontWeight = FontWeight.ExtraBold,
                            color = gymWhite,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
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
        // Scroll state para poder hacer scroll si es necesario
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState) // AÑADIDO: Scroll vertical
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de la rutina (más compacta)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (!currentRoutine?.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentRoutine?.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Imagen Ejercicio",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                    )
                                )
                        )
                        Text(
                            text = currentRoutine?.name?.uppercase() ?: "",
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            color = gymWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 0.5.sp
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = gymBorderGray,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "SIN IMAGEN",
                                    color = gymLightGray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timer más compacto
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = gymCardGray,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "TIEMPO DE DESCANSO",
                            color = gymAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 56.sp,
                            letterSpacing = 2.sp
                        ),
                        color = when {
                            timeLeft < 10 -> gymWarning
                            timeLeft < 30 -> gymAccent
                            else -> gymWhite
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Data boxes más compactos (pasos y estado)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentRoutine?.isStepCounterActive == true) {
                    DataBox(
                        label = "PASOS",
                        value = "$steps",
                        icon = Icons.Default.DirectionsRun,
                        color = gymAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
                DataBox(
                    label = "ESTADO",
                    value = "LIVE",
                    icon = Icons.Default.FiberManualRecord,
                    color = gymSuccess,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botones más compactos y optimizados
            Button(
                onClick = {
                    if (isRunning) isRunning = false
                    else {
                        hardware.stopAlarm()
                        isRunning = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp), // Altura reducida
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) gymBorderGray else gymAccent,
                    contentColor = if (isRunning) gymLightGray else Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isRunning) "PAUSAR DESCANSO" else "INICIAR DESCANSO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón finalizar más compacto
            OutlinedButton(
                onClick = {
                    hardware.stopAlarm()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = gymWarning
                ),
                border = BorderStroke(1.dp, gymWarning.copy(alpha = 0.5f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "FINALIZAR RUTINA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Frase motivacional pequeña
            Text(
                text = "💪 " + when {
                    isRunning && timeLeft > 0 -> "DESCANSA Y RECUPERA"
                    !isRunning && timeLeft > 0 -> "PREPARADO PARA CONTINUAR"
                    timeLeft == 0 -> "¡DESCANSO COMPLETADO!"
                    else -> "TÚ PUEDES, NO TE RINDAS"
                } + " 💪",
                color = gymLightGray.copy(alpha = 0.6f),
                fontSize = 9.sp,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun DataBox(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF121212),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF252525))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                label,
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
            Text(
                value,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}
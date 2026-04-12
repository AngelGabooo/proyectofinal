package com.angel.proyectofinal.features.routines.presentation.screens

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.angel.proyectofinal.core.services.TimerService
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

    // --- NUEVO: ESTADO PARA MANEJO DE RED (SNACKBAR) ---
    val snackbarHostState = remember { SnackbarHostState() }

    // Escuchar eventos de sincronización desde el ViewModel
    LaunchedEffect(Unit) {
        workoutViewModel.uiEvent.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    var completedSets by remember { mutableIntStateOf(0) }

    var timerService by remember { mutableStateOf<TimerService?>(null) }
    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as TimerService.TimerBinder
                timerService = binder.getService()
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                timerService = null
            }
        }
    }

    DisposableEffect(Unit) {
        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        onDispose { context.unbindService(connection) }
    }

    val currentRoutine = remember(routines, routineId) {
        routines.find { it.id == routineId }
    }

    val timeLeft by timerService?.timeLeft?.collectAsState() ?: remember(currentRoutine) {
        mutableIntStateOf(currentRoutine?.restTime ?: 90)
    }
    val isRunning by timerService?.isRunning?.collectAsState() ?: remember { mutableStateOf(false) }
    val steps by hardware.stepCountFlow.collectAsState()

    LaunchedEffect(Unit) { hardware.startStepCounter() }

    LaunchedEffect(timeLeft) {
        if (timeLeft == 0 && isRunning) {
            hardware.playWorkoutEndAlert()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            hardware.stopStepCounter()
            hardware.stopAlarm()
        }
    }

    val exercisesList = remember(currentRoutine) {
        currentRoutine?.exercises?.split("\n")?.filter { it.isNotBlank() } ?: emptyList()
    }
    val visibleExercises = exercisesList.take(3)
    val remainingExercises = exercisesList.size - 3

    Scaffold(
        containerColor = gymBlack,
        // --- CONFIGURACIÓN SNACKBAR PARA ALERTAS DE RED ---
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = gymCardGray,
                    contentColor = gymWhite,
                    actionColor = gymAccent,
                    snackbarData = data,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FitnessCenter, null, tint = gymAccent, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "ENTRENAMIENTO",
                            fontWeight = FontWeight.ExtraBold,
                            color = gymWhite,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = gymWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = gymDarkGray)
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Imagen más compacta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentRoutine?.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))))
                    )
                    Text(
                        currentRoutine?.name?.uppercase() ?: "",
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
                        color = gymWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // 2. Timer Compacto
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = gymCardGray,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            null,
                            tint = gymSuccess,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "SERIES: $completedSets",
                            color = gymSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 48.sp,
                            letterSpacing = 2.sp
                        ),
                        color = if (isRunning) gymAccent else gymWhite
                    )

                    Text(
                        text = if (isRunning) "DESCANSO" else "PREPARACIÓN",
                        color = gymLightGray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // 3. Ejercicios
            Text(
                "EJERCICIOS",
                color = gymAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(6.dp))

            visibleExercises.forEach { exercise ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    color = gymCardGray,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, gymBorderGray)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            null,
                            tint = if (completedSets > 0) gymSuccess else gymBorderGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            exercise,
                            color = gymWhite,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (remainingExercises > 0) {
                Text(
                    "+ $remainingExercises ejercicios más",
                    color = gymLightGray,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // 4. Data boxes
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (currentRoutine?.isStepCounterActive == true) {
                    CompactDataBox(
                        value = "$steps",
                        icon = Icons.Default.DirectionsRun,
                        color = gymAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
                CompactDataBox(
                    value = "LIVE",
                    icon = Icons.Default.FiberManualRecord,
                    color = gymSuccess,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // 5. Botón principal compacto
            Button(
                onClick = {
                    if (!isRunning) {
                        hardware.stopAlarm()
                        timerService?.startTimer(currentRoutine?.restTime ?: 90)
                        completedSets++
                    } else {
                        timerService?.stopTimer()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) gymBorderGray else gymAccent,
                    contentColor = if (isRunning) gymWhite else Color.Black
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        if (isRunning) Icons.Default.SkipNext else Icons.Default.Timer,
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (isRunning) "SALTAR" else "SERIE COMPLETADA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // 6. Botón finalizar compacto
            OutlinedButton(
                onClick = {
                    currentRoutine?.let { routine ->
                        workoutViewModel.finishWorkout(
                            routineId = routine.id,
                            routineName = routine.name,
                            steps = steps
                        )
                    }
                    timerService?.stopTimer()
                    hardware.stopAlarm()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = gymWarning),
                border = BorderStroke(1.dp, gymWarning.copy(alpha = 0.5f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("FINALIZAR", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "💪 " + when {
                    isRunning && timeLeft > 0 -> "DESCANSA"
                    !isRunning && timeLeft > 0 -> "PREPARADO"
                    timeLeft == 0 -> "¡COMPLETADO!"
                    else -> "TÚ PUEDES"
                } + " 💪",
                color = gymLightGray.copy(alpha = 0.5f),
                fontSize = 9.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CompactDataBox(
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(56.dp),
        color = Color(0xFF121212),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                value,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}
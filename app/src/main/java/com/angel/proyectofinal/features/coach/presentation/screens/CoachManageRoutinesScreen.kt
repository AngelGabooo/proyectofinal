package com.angel.proyectofinal.features.coach.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.coach.presentation.viewmodels.CoachViewModel

// Paleta de Colores Futurista
private val GymBlack = Color(0xFF080808) // Fondo ultra oscuro
private val CardBlack = Color(0xFF121212) // Tarjeta gris oscuro
private val NeonGreenStart = Color(0xFF00E676) // Verde Neón brillante
private val NeonGreenEnd = Color(0xFF00A152) // Verde Neón oscuro para degradado
private val ErrorRed = Color(0xFFFF1744) // Rojo brillante para borrar
private val TextGray = Color(0xFFAAAAAA) // Gris para textos secundarios

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachManageRoutinesScreen(navController: NavController, viewModel: CoachViewModel = hiltViewModel()) {
    val routines by viewModel.allRoutines.collectAsState()

    Scaffold(
        containerColor = GymBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "CENTRO DE MANDO",
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = GymBlack
                ),
                modifier = Modifier.padding(horizontal = 16.dp) // TopBar flotante
            )
        },
        floatingActionButton = {
            // FAB con degradado neón
            FloatingActionButton(
                onClick = { navController.navigate("create_routine") },
                containerColor = Color.Transparent,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(NeonGreenStart, NeonGreenEnd)),
                        shape = CircleShape
                    )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear", tint = Color.Black, modifier = Modifier.size(32.dp))
            }
        }
    ) { padding ->
        if (routines.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Inicia el clan creando una rutina", color = TextGray, fontWeight = FontWeight.Medium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(routines) { routine ->
                    ModernRoutineCard(
                        name = routine.name,
                        info = "${routine.dayOfWeek} • ${routine.objective}",
                        isVisible = routine.isVisible,
                        isStepsActive = routine.isStepCounterActive,
                        restTime = routine.restTime,
                        onDelete = { viewModel.deleteRoutine(routine) },
                        onVisibilityToggle = { viewModel.toggleVisibility(routine) },
                        onStepsToggle = { viewModel.toggleStepCounter(routine) }
                    )
                }
            }
        }
    }
}

@Composable
fun ModernRoutineCard(
    name: String,
    info: String,
    isVisible: Boolean,
    isStepsActive: Boolean,
    restTime: Int,
    onDelete: () -> Unit,
    onVisibilityToggle: (Boolean) -> Unit,
    onStepsToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBlack),
        shape = RoundedCornerShape(24.dp), // Bordes muy redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(Modifier.padding(24.dp)) {
            // Cabecera: Nombre y Borrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name.uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = info,
                        color = TextGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Botón borrar minimalista con borde rojo
                OutlinedIconAction(
                    icon = Icons.Default.Delete,
                    color = ErrorRed,
                    onClick = onDelete
                )
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFF252525), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            // Fila de Controles Visuables
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusControlItem(
                    label = "PÚBLICA",
                    icon = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    isActive = isVisible,
                    onClick = { onVisibilityToggle(!isVisible) }
                )
                StatusControlItem(
                    label = "PASOS",
                    icon = Icons.Default.DirectionsRun,
                    isActive = isStepsActive,
                    onClick = { onStepsToggle(!isStepsActive) }
                )
            }

            Spacer(Modifier.height(20.dp))

            // Visualizador de Tiempo Estilo "Gauge"
            TimeDisplayItem(restTime)
        }
    }
}

@Composable
fun StatusControlItem(label: String, icon: ImageVector, isActive: Boolean, onClick: () -> Unit) {
    val contentColor = if (isActive) NeonGreenStart else TextGray
    val borderColor = if (isActive) NeonGreenStart.copy(alpha = 0.5f) else Color(0xFF252525)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun TimeDisplayItem(restTime: Int) {
    Surface(
        color = Color(0xFF181818),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Timer, contentDescription = null, tint = NeonGreenStart, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text("DESCANSO ENTRE SERIES", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Text(
                text = "$restTime s",
                color = NeonGreenStart,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.background(NeonGreenStart.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun OutlinedIconAction(icon: ImageVector, color: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = CircleShape,
        modifier = Modifier.size(44.dp).border(1.dp, color.copy(alpha = 0.5f), CircleShape)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        }
    }
}
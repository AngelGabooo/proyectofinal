package com.angel.proyectofinal.features.coach.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.coach.presentation.viewmodels.CoachViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachManageRoutinesScreen(navController: NavController, viewModel: CoachViewModel = hiltViewModel()) {
    // Colores Oficiales FitPro UP
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)

    val routines by viewModel.allRoutines.collectAsState()

    Scaffold(
        containerColor = gymBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FitnessCenter, null, tint = gymAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CENTRO DE MANDO", fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, color = gymWhite, fontSize = 16.sp)
                    }
                },
                // BOTÓN DE REGRESO AÑADIDO
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_routine") },
                containerColor = gymAccent,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, "Crear", tint = Color.Black, modifier = Modifier.size(28.dp))
            }
        }
    ) { padding ->
        if (routines.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Inbox, null, tint = gymBorderGray, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("NO HAY RUTINAS", color = gymLightGray, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // LazyColumn gestiona el SCROLL automáticamente
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 100.dp), // Espacio extra abajo para el FAB
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(routines, key = { it.id }) { routine ->
                    ModernRoutineCard(
                        name = routine.name,
                        day = routine.dayOfWeek,
                        objective = routine.objective,
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
    day: String,
    objective: String,
    isVisible: Boolean,
    isStepsActive: Boolean,
    restTime: Int,
    onDelete: () -> Unit,
    onVisibilityToggle: (Boolean) -> Unit,
    onStepsToggle: (Boolean) -> Unit
) {
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)
    val gymSuccess = Color(0xFF00E676)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = gymCardGray),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, gymBorderGray)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // CABECERA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name.uppercase(), color = gymWhite, fontWeight = FontWeight.Black, fontSize = 18.sp, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BadgeItem(day, gymLightGray)
                        BadgeItem(objective, gymAccent)
                    }
                }

                Surface(
                    onClick = onDelete,
                    color = Color.Transparent,
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp).border(1.dp, gymAccent.copy(alpha = 0.3f), CircleShape)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Delete, null, tint = gymAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = gymBorderGray, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(20.dp))

            // CONTROLES (Botones más pequeños y eficientes)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatusControlItem(
                    label = "PÚBLICA",
                    icon = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    isActive = isVisible,
                    activeColor = gymAccent,
                    onClick = { onVisibilityToggle(!isVisible) },
                    modifier = Modifier.weight(1f)
                )
                StatusControlItem(
                    label = "PASOS",
                    icon = Icons.Default.DirectionsRun,
                    isActive = isStepsActive,
                    activeColor = gymSuccess,
                    onClick = { onStepsToggle(!isStepsActive) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TIEMPO DE DESCANSO
            Surface(
                color = Color(0xFF121212),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Timer, null, tint = gymAccent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("DESCANSO:", color = gymLightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("$restTime seg", color = gymWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun StatusControlItem(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val contentColor = if (isActive) activeColor else Color(0xFF8E8E93)
    val borderColor = if (isActive) activeColor.copy(alpha = 0.4f) else Color(0xFF2C2C2E)

    Surface(
        modifier = modifier.height(65.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF121212),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = contentColor, modifier = Modifier.size(20.dp))
            Text(label, color = contentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BadgeItem(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
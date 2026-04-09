package com.angel.proyectofinal.features.routines.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.angel.proyectofinal.features.routines.presentation.viewmodels.RoutinesViewModel
import com.angel.proyectofinal.features.routines.domain.entities.Routine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen(navController: NavController, viewModel: RoutinesViewModel = hiltViewModel()) {
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymAccent = Color(0xFFFF2D55)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)

    val routines by viewModel.routines.collectAsState(initial = emptyList())

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
                            null,
                            tint = gymAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "MIS RUTINAS",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = gymWhite,
                            fontSize = 18.sp
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
        if (routines.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = gymAccent.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(2.dp, gymAccent)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.FitnessCenter, null, tint = gymAccent, modifier = Modifier.size(40.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("CARGANDO RUTINAS...", color = gymWhite, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = gymAccent, modifier = Modifier.size(32.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = routines, key = { it.id }) { routine ->
                    RoutineCard(
                        routine = routine,
                        accentColor = gymAccent,
                        onClick = { navController.navigate("workout/${routine.id}") }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💪 ¡SIGUE ENTRENANDO DURO! 💪",
                        color = gymLightGray.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RoutineCard(routine: Routine, accentColor: Color, onClick: () -> Unit) {
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymSuccess = Color(0xFF00E676)
    val gymWarning = Color(0xFFFFB300)

    // CORRECCIÓN: Forma correcta de detectar presión para animación de escala
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "scale"
    )

    val statusColor = when (routine.status.uppercase()) {
        "COMPLETADO" -> gymSuccess
        "EN PROGRESO" -> accentColor
        else -> gymWarning
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current, // Mantiene el ripple visual
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = gymCardGray),
        border = androidx.compose.foundation.BorderStroke(1.dp, gymBorderGray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2C2C2E)),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(routine.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = accentColor, modifier = Modifier.size(24.dp))
                        }
                    },
                    error = {
                        Icon(Icons.Default.FitnessCenter, null, tint = accentColor.copy(alpha = 0.5f), modifier = Modifier.size(32.dp))
                    }
                )
                if (routine.status.uppercase() == "COMPLETADO") {
                    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))) {
                        Icon(Icons.Default.CheckCircle, null, tint = gymSuccess, modifier = Modifier.align(Alignment.Center).size(32.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(routine.name.uppercase(), color = gymWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(30.dp), color = gymLightGray.copy(alpha = 0.15f)) {
                        Text(routine.dayOfWeek, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = gymLightGray, fontSize = 10.sp)
                    }
                    Text("•", color = gymLightGray, fontSize = 10.sp)
                    Surface(shape = RoundedCornerShape(30.dp), color = accentColor.copy(alpha = 0.15f)) {
                        Text(routine.objective, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = accentColor, fontSize = 10.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Surface(color = statusColor.copy(alpha = 0.15f), shape = RoundedCornerShape(30.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Box(Modifier.size(6.dp).clip(CircleShape).background(statusColor))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(routine.status.uppercase(), color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Surface(modifier = Modifier.size(32.dp), shape = CircleShape, color = accentColor.copy(alpha = 0.1f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.ChevronRight, null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
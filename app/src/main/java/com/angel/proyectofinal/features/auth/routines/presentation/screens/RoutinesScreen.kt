package com.angel.proyectofinal.features.routines.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.routines.presentation.viewmodels.RoutinesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen(navController: NavController, viewModel: RoutinesViewModel = hiltViewModel()) {
    // Usamos collectAsState con un valor inicial vacío para evitar errores de nulidad
    val routines by viewModel.routines.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ENTRENAMIENTOS",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            )
        }
    ) { padding ->
        // VALIDACIÓN: Si la lista está cargando o está vacía, mostramos un indicador
        if (routines.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF00C853))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(routines) { routine ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("workout/${routine.id}") },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.05f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    // Etiqueta de Estado (Status)
                                    Surface(
                                        color = Color(0xFF00C853).copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(100.dp)
                                    ) {
                                        Text(
                                            text = (routine.status ?: "PENDIENTE").uppercase(),
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF00C853),
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    // Nombre de la Rutina
                                    Text(
                                        text = routine.name ?: "Nueva Rutina",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    // Descripción (Día y Objetivo)
                                    Text(
                                        text = "${routine.dayOfWeek} • ${routine.objective}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray,
                                        maxLines = 1
                                    )
                                }

                                // Botón Circular de Play
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    color = Color(0xFF00C853),
                                    shape = RoundedCornerShape(100.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Empezar",
                                        tint = Color.Black,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
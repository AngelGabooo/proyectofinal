package com.angel.proyectofinal.features.coach.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachHomeScreen(navController: NavController) {
    // Colores GymStyle consistentes con tu diseño
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymAccent = Color(0xFFFF2D55) // Rojo neón

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
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "PANEL COACH",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = gymDarkGray
                )
            )
        }
    ) { padding ->
        // LazyVerticalGrid permite Scroll automático y mantiene el diseño de 2 columnas
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera de bienvenida (Ocupa las 2 columnas con span)
            item(span = { GridItemSpan(2) }) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¡BIENVENIDO, COACH!",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Gestiona el progreso de tu clan hoy",
                        color = Color(0xFF8E8E93),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // --- BOTÓN GESTIONAR ---
            item {
                CoachMenuCard(
                    title = "GESTIONAR",
                    subtitle = "Tus rutinas",
                    icon = Icons.Default.List,
                    color = Color(0xFF00E676), // Verde Neón
                    onClick = { navController.navigate("coach_manage") }
                )
            }

            // --- BOTÓN CREAR ---
            item {
                CoachMenuCard(
                    title = "CREAR",
                    subtitle = "Nueva rutina",
                    icon = Icons.Default.AddCircle,
                    color = Color(0xFF00D4FF), // Azul Neón
                    onClick = { navController.navigate("create_routine") }
                )
            }

            // --- BOTÓN PERFIL ---
            item {
                CoachMenuCard(
                    title = "PERFIL",
                    subtitle = "Tu cuenta",
                    icon = Icons.Default.Person,
                    color = Color(0xFFFFD700), // Oro
                    onClick = { navController.navigate("profile") }
                )
            }

            // --- BOTÓN SALIR ---
            item {
                CoachMenuCard(
                    title = "SALIR",
                    subtitle = "Cerrar sesión",
                    icon = Icons.Default.Logout,
                    color = gymAccent, // Rojo
                    onClick = {
                        navController.navigate("login") {
                            // Limpia el historial para que no pueda volver atrás al panel
                            popUpTo("coach_panel") { inclusive = true }
                        }
                    }
                )
            }

            // Espacio extra al final del scroll
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun CoachMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2C2E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.12f) // Fondo sutil con el color del icono
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 0.5.sp
            )
            Text(
                text = subtitle,
                color = Color(0xFF8E8E93),
                fontSize = 11.sp
            )
        }
    }
}
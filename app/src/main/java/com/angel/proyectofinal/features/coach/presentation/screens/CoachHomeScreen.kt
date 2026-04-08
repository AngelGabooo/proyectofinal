package com.angel.proyectofinal.features.coach.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachHomeScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PANEL COACH", fontWeight = FontWeight.Black, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF1E1E1E))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "¡Bienvenido, Coach!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Gestiona el progreso de tu clan hoy",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    CoachMenuCard(
                        title = "Gestionar",
                        icon = Icons.Default.List,
                        color = Color(0xFF00C853),
                        onClick = { navController.navigate("coach_manage") }
                    )
                }
                item {
                    CoachMenuCard(
                        title = "Crear Rutina",
                        icon = Icons.Default.AddCircle,
                        color = Color(0xFF00B0FF),
                        onClick = { navController.navigate("create_routine") }
                    )
                }
                item {
                    CoachMenuCard(
                        title = "Mi Perfil",
                        icon = Icons.Default.Person,
                        color = Color(0xFFFFD600),
                        onClick = { navController.navigate("profile") }
                    )
                }
                item {
                    CoachMenuCard(
                        title = "Cerrar Sesión",
                        icon = Icons.Default.ExitToApp,
                        color = Color(0xFFFF5252),
                        onClick = {
                            navController.navigate("login") {
                                popUpTo("coach_home") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CoachMenuCard(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
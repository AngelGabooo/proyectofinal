package com.angel.proyectofinal.features.profile.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // Importado
import androidx.compose.foundation.verticalScroll // Importado
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.profile.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    // Colores GymStyle
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)
    val gymSuccess = Color(0xFF00E676)

    // Datos del usuario
    val userName = "Ángel García"
    val userEmail = "angel@fitpro.com"
    val memberSince = "Enero 2024"
    val membershipType = "Premium"
    val daysActive = 124

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
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "MI PERFIL",
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
            // TARJETA DE PERFIL CON AVATAR
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = gymAccent.copy(alpha = 0.15f),
                        border = BorderStroke(2.dp, gymAccent)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = gymAccent,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = userName.uppercase(),
                        color = gymWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = gymLightGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = userEmail,
                            color = gymLightGray,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(30.dp),
                        color = gymAccent.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = gymAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = membershipType.uppercase(),
                                color = gymAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            // TARJETA DE ESTADÍSTICAS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Analytics,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "ESTADÍSTICAS PERSONALES",
                            color = gymLightGray,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "DÍAS ACTIVOS",
                            value = daysActive.toString(),
                            icon = Icons.Default.FlashOn,
                            color = gymAccent,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "MIEMBRO DESDE",
                            value = memberSince,
                            icon = Icons.Default.DateRange,
                            color = Color(0xFF00D4FF),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "RUTINAS",
                            value = "24",
                            icon = Icons.Default.FitnessCenter,
                            color = Color(0xFFFFB300),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "LOGROS",
                            value = "8",
                            icon = Icons.Default.EmojiEvents,
                            color = gymSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // TARJETA DE OPCIONES
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = gymCardGray),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "OPCIONES",
                            color = gymLightGray,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    ProfileOption(
                        icon = Icons.Default.Lock,
                        title = "CAMBIAR CONTRASEÑA",
                        subtitle = "Actualiza tu contraseña",
                        onClick = { /* Navegar */ }
                    )

                    HorizontalDivider(color = gymBorderGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                    ProfileOption(
                        icon = Icons.Default.Notifications,
                        title = "NOTIFICACIONES",
                        subtitle = "Configura tus alertas",
                        onClick = { /* Navegar */ }
                    )

                    HorizontalDivider(color = gymBorderGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                    ProfileOption(
                        icon = Icons.Default.Info,
                        title = "ACERCA DE",
                        subtitle = "Información de la app",
                        onClick = { /* Navegar */ }
                    )
                }
            }

            // BOTÓN CERRAR SESIÓN
            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = gymCardGray),
                border = BorderStroke(1.dp, gymBorderGray)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = gymAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold, color = gymAccent)
                }
            }

            Text(
                text = "FITPRO UP v1.0.0",
                color = gymLightGray.copy(alpha = 0.4f),
                fontSize = 10.sp,
                modifier = Modifier.padding(bottom = 16.dp) // Aumentado padding inferior
            )
        }
    }
}

// --- Componentes auxiliares (Se mantienen igual) ---

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        border = BorderStroke(1.dp, Color(0xFF2C2C2E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = color.copy(alpha = 0.15f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = Color(0xFF8E8E93), fontSize = 9.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
            Text(value, color = Color(0xFFFFFFFF), fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ProfileOption(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color(0xFFFF2D55).copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Color(0xFFFF2D55), modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = Color(0xFF8E8E93), fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF8E8E93), modifier = Modifier.size(20.dp))
    }
}
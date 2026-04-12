package com.angel.proyectofinal.features.profile.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.profile.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)
    val gymSuccess = Color(0xFF00E676)

    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Lanzar autenticación al entrar
    LaunchedEffect(Unit) {
        if (!isAuthenticated) {
            viewModel.authenticate(context as FragmentActivity)
        }
    }

    if (!isAuthenticated) {
        // PANTALLA DE BLOQUEO
        Box(modifier = Modifier.fillMaxSize().background(gymBlack), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Lock, "Bloqueado", tint = gymAccent, modifier = Modifier.size(64.dp))
                Spacer(Modifier.height(16.dp))
                Text("PERFIL PROTEGIDO", color = gymWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
                Text("Se requiere autenticación para continuar", color = gymLightGray, fontSize = 12.sp)
                Spacer(Modifier.height(32.dp))
                Button(onClick = { viewModel.authenticate(context as FragmentActivity) }, colors = ButtonDefaults.buttonColors(containerColor = gymAccent)) {
                    Text("DESBLOQUEAR", color = Color.White, fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = { navController.popBackStack() }) { Text("VOLVER", color = gymLightGray) }
            }
        }
    } else {
        Scaffold(
            containerColor = gymBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = gymAccent, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("MI PERFIL", fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, color = gymWhite, fontSize = 20.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Volver", tint = gymWhite)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = gymDarkGray)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TARJETA DE PERFIL DINÁMICA
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = gymCardGray),
                    border = BorderStroke(1.dp, if (viewModel.isCoach) Color(0xFFFFD700) else gymBorderGray)
                ) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(100.dp),
                            shape = CircleShape,
                            color = if (viewModel.isCoach) Color(0xFFFFD700).copy(alpha = 0.1f) else gymAccent.copy(alpha = 0.15f),
                            border = BorderStroke(2.dp, if (viewModel.isCoach) Color(0xFFFFD700) else gymAccent)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    if (viewModel.isCoach) Icons.Default.VerifiedUser else Icons.Default.Person,
                                    null,
                                    tint = if (viewModel.isCoach) Color(0xFFFFD700) else gymAccent,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))

                        // Nombre condicional
                        Text(
                            text = if (viewModel.isCoach) "COACH PRINCIPAL" else viewModel.userName.uppercase(),
                            color = gymWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Correo dinámico
                        Text(viewModel.userEmail, color = gymLightGray, fontSize = 13.sp)

                        if (viewModel.isCoach) {
                            Spacer(Modifier.height(8.dp))
                            Surface(color = Color(0xFFFFD700).copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    "GYM: UP FITNESS CENTER",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    color = Color(0xFFFFD700),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                // MOSTRAR ESTADÍSTICAS SOLO SI NO ES EL COACH
                if (!viewModel.isCoach) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = gymCardGray),
                        border = BorderStroke(1.dp, gymBorderGray)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Analytics, null, tint = gymAccent, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("ESTADÍSTICAS PERSONALES", color = gymLightGray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatCard("DÍAS ACTIVOS", "124", Icons.Default.FlashOn, gymAccent, Modifier.weight(1f))
                                StatCard("MIEMBRO DESDE", "Enero 2024", Icons.Default.DateRange, Color(0xFF00D4FF), Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    // TARJETA ESPECIAL PARA EL COACH (En lugar de stats)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = gymCardGray),
                        border = BorderStroke(1.dp, Color(0xFF2C2C2E))
                    ) {
                        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, null, tint = Color(0xFF00D4FF), modifier = Modifier.size(32.dp))
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("MODO ADMINISTRADOR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Acceso total a gestión de rutinas y clan", color = gymLightGray, fontSize = 11.sp)
                            }
                        }
                    }
                }

                // TARJETA DE OPCIONES (Mantenemos el diseño)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = gymCardGray),
                    border = BorderStroke(1.dp, gymBorderGray)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        ProfileOption(Icons.Default.Lock, "SEGURIDAD", "Configurar credenciales") {}
                        HorizontalDivider(color = gymBorderGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        ProfileOption(Icons.Default.Notifications, "NOTIFICACIONES", "Alertas del sistema") {}
                        HorizontalDivider(color = gymBorderGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        ProfileOption(Icons.Default.HelpCenter, "SOPORTE", "Centro de ayuda") {}
                    }
                }

                Button(
                    onClick = {
                        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = gymCardGray),
                    border = BorderStroke(1.dp, gymBorderGray)
                ) {
                    Icon(Icons.Default.Logout, null, tint = gymAccent, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold, color = gymAccent)
                }

                Text("FITPRO UP v1.0.0", color = gymLightGray.copy(alpha = 0.4f), fontSize = 10.sp, modifier = Modifier.padding(bottom = 16.dp))
            }
        }
    }
}

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
                    Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = Color(0xFF8E8E93), fontSize = 9.sp, fontWeight = FontWeight.Medium)
            Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileOption(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color(0xFFFF2D55).copy(alpha = 0.1f)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFFFF2D55), modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = Color(0xFF8E8E93), fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF8E8E93), modifier = Modifier.size(20.dp))
    }
}
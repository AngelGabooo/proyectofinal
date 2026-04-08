package com.angel.proyectofinal.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity // <--- Verifica que este import esté presente
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.core.utils.HardwareHelper
import com.angel.proyectofinal.features.profile.presentation.viewmodels.ProfileViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val context = LocalContext.current
    // Hacemos el cast de forma segura a la clase de Android
    val activity = context as? FragmentActivity
    val hardware = remember { HardwareHelper(context) }

    var isAuthorized by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableIntStateOf(10) }
    var showTimer by remember { mutableStateOf(false) }

    val launchAuthentication = {
        if (hardware.canUseBiometric()) {
            activity?.let {
                hardware.showBiometricPrompt(it) {
                    isAuthorized = true
                }
            }
        } else {
            isAuthorized = true
        }
    }

    LaunchedEffect(Unit) {
        launchAuthentication()
        delay(1500)
        showTimer = true
    }

    LaunchedEffect(showTimer) {
        if (showTimer && !isAuthorized) {
            while (secondsRemaining > 0 && !isAuthorized) {
                delay(1000)
                secondsRemaining--
            }
            if (secondsRemaining == 0 && !isAuthorized) {
                launchAuthentication()
            }
        }
    }

    if (!isAuthorized) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F0F)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { secondsRemaining / 10f },
                        modifier = Modifier.size(100.dp),
                        color = Color(0xFF00C853),
                        strokeWidth = 8.dp,
                        trackColor = Color.DarkGray
                    )
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text("AUTENTICACIÓN REQUERIDA", color = Color.White, fontWeight = FontWeight.Black)
                Text(
                    if (secondsRemaining > 0) "Esperando huella... ($secondsRemaining s)"
                    else "Iniciando validación por PIN...",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(60.dp))
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("CANCELAR ACCESO", color = Color.Red.copy(alpha = 0.7f))
                }
            }
        }
    } else {
        Scaffold(
            containerColor = Color(0xFF0F0F0F),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("PERFIL PREMIUM", fontWeight = FontWeight.Black) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF1E1E1E),
                        titleContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(100.dp), color = Color(0xFF00C853)) {
                            Icon(Icons.Default.VerifiedUser, null, tint = Color.Black, modifier = Modifier.padding(12.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("Angel", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Miembro desde 2026", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Text("SUSCRIPCIÓN", color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Plan Anual PRO", color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
                        Text("Tu acceso vence el 12 de Diciembre, 2026", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navController.navigate("login") { popUpTo("home") { inclusive = true } } },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Icon(Icons.Default.Logout, null, tint = Color.Red)
                    Spacer(Modifier.width(12.dp))
                    Text("CERRAR SESIÓN", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
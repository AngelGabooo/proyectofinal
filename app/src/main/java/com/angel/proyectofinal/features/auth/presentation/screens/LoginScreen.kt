package com.angel.proyectofinal.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.auth.presentation.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)

    Surface(modifier = Modifier.fillMaxSize(), color = gymBlack) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(gymDarkGray, gymBlack)))) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = gymCardGray,
                    modifier = Modifier.size(80.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, gymAccent)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.FitnessCenter, null, tint = gymAccent, modifier = Modifier.size(48.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("FITPRO UP", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = gymWhite, fontSize = 32.sp)
                Text("ENTRENA CON LOS MEJORES", color = gymAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(48.dp))

                // Email Field
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("CORREO ELECTRÓNICO", color = gymLightGray, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = gymAccent, modifier = Modifier.size(20.dp)) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gymAccent, unfocusedBorderColor = gymBorderGray, focusedTextColor = gymWhite, unfocusedTextColor = gymWhite)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password Field
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("CONTRASEÑA", color = gymLightGray, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = gymAccent, modifier = Modifier.size(20.dp)) },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gymAccent, unfocusedBorderColor = gymBorderGray, focusedTextColor = gymWhite, unfocusedTextColor = gymWhite)
                )

                // Error Message
                viewModel.errorMessage?.let {
                    Text(it, color = gymAccent, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = {
                        viewModel.login { isCoach ->
                            if (isCoach) {
                                navController.navigate("coach_panel") { popUpTo("login") { inclusive = true } }
                            } else {
                                navController.navigate("home") { popUpTo("login") { inclusive = true } }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = gymAccent, contentColor = gymWhite),
                    enabled = !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = gymWhite, modifier = Modifier.size(24.dp))
                    } else {
                        Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(onClick = { navController.navigate("register") }) {
                    Text("¿Eres nuevo? CREA UNA CUENTA", color = gymAccent, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = gymBorderGray, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 32.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("🔥 SIN EXCUSAS, SOLO RESULTADOS 🔥", color = gymAccent.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
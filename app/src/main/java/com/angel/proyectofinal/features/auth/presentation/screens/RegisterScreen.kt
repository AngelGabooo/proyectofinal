package com.angel.proyectofinal.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.angel.proyectofinal.features.auth.presentation.viewmodels.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
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
                modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp), color = gymCardGray,
                    modifier = Modifier.size(70.dp), border = androidx.compose.foundation.BorderStroke(2.dp, gymAccent)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.FitnessCenter, null, tint = gymAccent, modifier = Modifier.size(42.dp))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("ÚNETE AL CLAN", fontWeight = FontWeight.ExtraBold, color = gymWhite, fontSize = 28.sp)
                Text("TU TRANSFORMACIÓN COMIENZA HOY", color = gymAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(30.dp))

                // Campo Nombre
                OutlinedTextField(
                    value = viewModel.fullName,
                    onValueChange = { viewModel.fullName = it },
                    label = { Text("NOMBRE COMPLETO", color = gymLightGray, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = gymAccent) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gymAccent, unfocusedBorderColor = gymBorderGray, focusedTextColor = gymWhite, unfocusedTextColor = gymWhite)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Email
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("CORREO ELECTRÓNICO", color = gymLightGray, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = gymAccent) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gymAccent, unfocusedBorderColor = gymBorderGray, focusedTextColor = gymWhite, unfocusedTextColor = gymWhite)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Password
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("CONTRASEÑA", color = gymLightGray, fontSize = 11.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = gymAccent) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gymAccent, unfocusedBorderColor = gymBorderGray, focusedTextColor = gymWhite, unfocusedTextColor = gymWhite)
                )

                // Recomendación visual de seguridad
                Text(
                    "Usa 6+ caracteres, letras y números",
                    color = gymLightGray.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Confirm Password
                OutlinedTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    label = { Text("CONFIRMAR CONTRASEÑA", color = gymLightGray, fontSize = 11.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = gymAccent) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = gymAccent, unfocusedBorderColor = gymBorderGray, focusedTextColor = gymWhite, unfocusedTextColor = gymWhite)
                )

                // Mensaje de Error Dinámico
                viewModel.errorMessage?.let {
                    Text(
                        text = it,
                        color = gymAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.registerUser {
                        navController.navigate("home") { popUpTo("register") { inclusive = true } }
                    }},
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = gymAccent, contentColor = gymWhite),
                    enabled = !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = gymWhite, modifier = Modifier.size(24.dp))
                    } else {
                        Text("REGISTRARME", fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("¿YA TIENES CUENTA? INICIA SESIÓN", color = gymAccent, fontSize = 13.sp)
                }
            }
        }
    }
}
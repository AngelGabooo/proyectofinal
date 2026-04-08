package com.angel.proyectofinal.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    // Colores GymStyle - Rojo/Energía (consistente con Login)
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55) // Rojo neón vibrante

    // Estados para los campos
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = gymBlack
    ) {
        // Fondo con gradiente sutil
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(gymDarkGray, gymBlack),
                        startY = 0f,
                        endY = 1000f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo con borde rojo
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = gymCardGray,
                    modifier = Modifier.size(70.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, gymAccent)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            contentDescription = "Logo",
                            tint = gymAccent,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "ÚNETE AL CLAN",
                    fontWeight = FontWeight.ExtraBold,
                    color = gymWhite,
                    fontSize = 28.sp,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "TU TRANSFORMACIÓN COMIENZA HOY",
                    color = gymAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Nombre Completo Field
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = {
                        Text(
                            "NOMBRE COMPLETO",
                            color = gymLightGray,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = gymAccent,
                        unfocusedBorderColor = gymBorderGray,
                        focusedLabelColor = gymAccent,
                        unfocusedLabelColor = gymLightGray,
                        cursorColor = gymAccent,
                        focusedTextColor = gymWhite,
                        unfocusedTextColor = gymLightGray
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = gymWhite,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            "CORREO ELECTRÓNICO",
                            color = gymLightGray,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = gymAccent,
                        unfocusedBorderColor = gymBorderGray,
                        focusedLabelColor = gymAccent,
                        unfocusedLabelColor = gymLightGray,
                        cursorColor = gymAccent,
                        focusedTextColor = gymWhite,
                        unfocusedTextColor = gymLightGray
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = gymWhite,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            "CONTRASEÑA",
                            color = gymLightGray,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = gymAccent,
                        unfocusedBorderColor = gymBorderGray,
                        focusedLabelColor = gymAccent,
                        unfocusedLabelColor = gymLightGray,
                        cursorColor = gymAccent,
                        focusedTextColor = gymWhite,
                        unfocusedTextColor = gymLightGray
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = gymWhite,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirmar Contraseña Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = {
                        Text(
                            "CONFIRMAR CONTRASEÑA",
                            color = gymLightGray,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = gymAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = gymAccent,
                        unfocusedBorderColor = gymBorderGray,
                        focusedLabelColor = gymAccent,
                        unfocusedLabelColor = gymLightGray,
                        cursorColor = gymAccent,
                        focusedTextColor = gymWhite,
                        unfocusedTextColor = gymLightGray
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = gymWhite,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Register Button
                Button(
                    onClick = {
                        // Aquí iría la lógica de registro
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gymAccent,
                        contentColor = gymWhite
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Text(
                        text = "REGISTRARME",
                        color = gymWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Back to Login Link
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "¿YA TIENES CUENTA? INICIA SESIÓN",
                        color = gymAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Línea decorativa
                Divider(
                    color = gymBorderGray,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Texto motivacional
                Text(
                    text = "💪 EL PRIMER PASO ES EL MÁS IMPORTANTE 💪",
                    color = gymAccent.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
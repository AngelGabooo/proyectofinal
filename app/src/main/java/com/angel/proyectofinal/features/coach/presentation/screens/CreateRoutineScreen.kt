package com.angel.proyectofinal.features.coach.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack // Importamos el icono de regreso
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.coach.presentation.viewmodels.CoachViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineScreen(navController: NavController, viewModel: CoachViewModel = hiltViewModel()) {
    // Colores GymStyle
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymCardGray = Color(0xFF1C1C1E)
    val gymBorderGray = Color(0xFF2C2C2E)
    val gymWhite = Color(0xFFFFFFFF)
    val gymLightGray = Color(0xFF8E8E93)
    val gymAccent = Color(0xFFFF2D55)
    val gymSuccess = Color(0xFF00E676)

    var name by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("90") }

    var exName by remember { mutableStateOf("") }
    var exSets by remember { mutableStateOf("") }
    var exReps by remember { mutableStateOf("") }

    val exercises = remember { mutableStateListOf<String>() }

    // Configuración de colores para campos de texto
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = gymWhite,
        unfocusedTextColor = gymWhite,
        focusedLabelColor = gymAccent,
        unfocusedLabelColor = gymLightGray,
        focusedBorderColor = gymAccent,
        unfocusedBorderColor = gymBorderGray,
        cursorColor = gymAccent
    )

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
                            "NUEVA RUTINA",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = gymWhite,
                            fontSize = 18.sp
                        )
                    }
                },
                // AÑADIMOS EL BOTÓN DE REGRESO AQUÍ
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = gymWhite
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "DATOS BÁSICOS",
                    color = gymAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la rutina") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = day,
                    onValueChange = { day = it },
                    label = { Text("Día (Ej. Lunes)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = objective,
                    onValueChange = { objective = it },
                    label = { Text("Objetivo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = restTime,
                    onValueChange = { restTime = it },
                    label = { Text("Descanso (Segundos)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "EJERCICIOS",
                    color = gymAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = gymCardGray),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, gymBorderGray)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = exName,
                            onValueChange = { exName = it },
                            label = { Text("Nombre del ejercicio") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = exSets,
                                onValueChange = { exSets = it },
                                label = { Text("Sets") },
                                modifier = Modifier.weight(1f),
                                colors = textFieldColors,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = exReps,
                                onValueChange = { exReps = it },
                                label = { Text("Reps") },
                                modifier = Modifier.weight(1f),
                                colors = textFieldColors,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Button(
                            onClick = {
                                if (exName.isNotBlank()) {
                                    exercises.add("${exName.uppercase()}: ${exSets}x${exReps}")
                                    exName = ""
                                    exSets = ""
                                    exReps = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = gymAccent,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("AÑADIR EJERCICIO", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            items(exercises) { ex ->
                Surface(
                    color = gymCardGray,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, gymBorderGray)
                ) {
                    Text(
                        "✓ $ex",
                        color = gymSuccess,
                        modifier = Modifier.padding(14.dp),
                        fontSize = 13.sp
                    )
                }
            }

            item {
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && exercises.isNotEmpty()) {
                            viewModel.saveRoutine(name, day, objective, exercises, restTime)
                            navController.navigate("coach_manage") {
                                popUpTo("coach_home") { inclusive = false }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gymSuccess,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        "GUARDAR RUTINA",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
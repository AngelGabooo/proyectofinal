package com.angel.proyectofinal.features.coach.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
    var name by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("90") }

    var exName by remember { mutableStateOf("") }
    var exSets by remember { mutableStateOf("") }
    var exReps by remember { mutableStateOf("") }

    val exercises = remember { mutableStateListOf<String>() }

    // Configuración de colores para todos los campos de texto
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,            // Letras blancas al escribir
        unfocusedTextColor = Color.White,          // Letras blancas al terminar
        focusedLabelColor = Color(0xFF00C853),     // Etiqueta verde al seleccionar
        unfocusedLabelColor = Color.Gray,          // Etiqueta gris al estar inactivo
        focusedBorderColor = Color(0xFF00C853),    // Borde verde neón
        unfocusedBorderColor = Color.DarkGray,     // Borde gris oscuro
        cursorColor = Color(0xFF00C853)            // Cursor verde
    )

    Scaffold(
        containerColor = Color(0xFF080808), // Negro profundo
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("NUEVA RUTINA", fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF121212))
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("DATOS BÁSICOS", color = Color(0xFF00C853), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)

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
                Text("EJERCICIOS", color = Color(0xFF00C853), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.DarkGray)
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

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                if(exName.isNotBlank()){
                                    exercises.add("$exName: $exSets x $exReps")
                                    exName = ""; exSets = ""; exReps = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Añadir Ejercicio", color = Color.White)
                        }
                    }
                }
            }

            items(exercises) { ex ->
                Surface(
                    color = Color(0xFF1A1A1A),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "✓ $ex",
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        if(name.isNotBlank() && exercises.isNotEmpty()) {
                            viewModel.saveRoutine(name, day, objective, exercises, restTime)
                            navController.navigate("coach_manage") {
                                popUpTo("coach_home") { inclusive = false }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                ) {
                    Text("GUARDAR RUTINA", color = Color.Black, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
            }
        }
    }
}
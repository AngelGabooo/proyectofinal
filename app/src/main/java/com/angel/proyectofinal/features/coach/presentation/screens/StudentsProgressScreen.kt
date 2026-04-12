package com.angel.proyectofinal.features.coach.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angel.proyectofinal.features.coach.presentation.viewmodels.CoachViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsProgressScreen(navController: NavController, viewModel: CoachViewModel = hiltViewModel()) {
    val gymBlack = Color(0xFF000000)
    val gymDarkGray = Color(0xFF0A0A0A)
    val gymAccent = Color(0xFFFF2D55)
    val gymBorderGray = Color(0xFF2C2C2E)

    val progressList by viewModel.studentsProgress.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Agrupamos el historial por el nombre del usuario
    val groupedProgress = remember(progressList, searchQuery) {
        progressList
            .filter {
                val name = (it["userName"] as? String) ?: ""
                name.contains(searchQuery, ignoreCase = true)
            }
            .groupBy { (it["userName"] as? String) ?: "Alumno Anónimo" }
    }

    Scaffold(
        containerColor = gymBlack,
        topBar = {
            Column(modifier = Modifier.background(gymDarkGray)) {
                CenterAlignedTopAppBar(
                    title = { Text("AVANCES DEL CLAN", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = gymDarkGray)
                )
                // BUSCADOR
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar alumno...", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = gymAccent) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = gymAccent,
                        unfocusedBorderColor = gymBorderGray,
                        focusedTextColor = Color.White
                    )
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    ) { padding ->
        if (groupedProgress.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("SIN RESULTADOS", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                groupedProgress.forEach { (studentName, history) ->
                    item {
                        StudentGroupHeader(
                            name = studentName,
                            historyCount = history.size,
                            historyItems = history,
                            onDelete = { ts -> viewModel.deleteStudentProgress(ts) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentGroupHeader(
    name: String,
    historyCount: Int,
    historyItems: List<Map<String, Any>>,
    onDelete: (Long) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val gymAccent = Color(0xFFFF2D55)
    val gymCardGray = Color(0xFF1C1C1E)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = gymCardGray),
        border = BorderStroke(1.dp, if(isExpanded) gymAccent else Color(0xFF2C2C2E))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = gymAccent.copy(alpha = 0.1f)
                ) {
                    Icon(Icons.Default.Person, null, tint = gymAccent, modifier = Modifier.padding(8.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(name.uppercase(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp)
                    Text("$historyCount entrenamientos registrados", color = Color.Gray, fontSize = 11.sp)
                }
                Icon(
                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    null,
                    tint = Color.Gray
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Divider(color = Color(0xFF2C2C2E), thickness = 0.5.dp)
                    historyItems.forEach { item ->
                        historyDetailRow(item, onDelete)
                    }
                }
            }
        }
    }
}

@Composable
fun historyDetailRow(data: Map<String, Any>, onDelete: (Long) -> Unit) {
    val routine = (data["routineName"] as? String) ?: "Rutina"
    val steps = data["steps"]?.toString() ?: "0"
    val timestamp = (data["timestamp"] as? Long) ?: 0L
    val date = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).format(Date(timestamp))

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(routine, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(date, color = Color.Gray, fontSize = 10.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 12.dp)) {
                Text(steps, color = Color(0xFF00E676), fontWeight = FontWeight.Black, fontSize = 14.sp)
                Text("PASOS", color = Color(0xFF00E676), fontSize = 7.sp, fontWeight = FontWeight.Bold)
            }
            IconButton(
                onClick = { onDelete(timestamp) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.Delete, null, tint = Color(0xFFFF2D55).copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
            }
        }
    }
}
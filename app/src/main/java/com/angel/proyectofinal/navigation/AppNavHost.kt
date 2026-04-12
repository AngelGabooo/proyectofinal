package com.angel.proyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angel.proyectofinal.features.auth.presentation.screens.LoginScreen
import com.angel.proyectofinal.features.auth.presentation.screens.RegisterScreen
import com.angel.proyectofinal.features.coach.presentation.screens.CoachHomeScreen
import com.angel.proyectofinal.features.coach.presentation.screens.CoachManageRoutinesScreen
import com.angel.proyectofinal.features.coach.presentation.screens.CreateRoutineScreen
import com.angel.proyectofinal.features.coach.presentation.screens.StudentsProgressScreen // IMPORTAR NUEVA PANTALLA
import com.angel.proyectofinal.features.home.presentation.screens.HomeScreen
import com.angel.proyectofinal.features.profile.presentation.screens.ProfileScreen
import com.angel.proyectofinal.features.routines.presentation.screens.RoutinesScreen
import com.angel.proyectofinal.features.routines.presentation.screens.WorkoutScreen
import com.angel.proyectofinal.features.stats.presentation.screens.StatsScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // --- LÓGICA DE AUTO-LOGIN (Premium UX) ---
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val coachEmail = "coach@fitpro.com"

    val startDestination = if (currentUser != null) {
        if (currentUser.email?.lowercase() == coachEmail.lowercase()) "coach_panel" else "home"
    } else {
        "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination, // Inicia donde corresponda la sesión
        modifier = modifier
    ) {
        // --- AUTENTICACIÓN ---
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        // --- FLUJO ALUMNO (USUARIO) ---
        composable("home") { HomeScreen(navController) }
        composable("routines") { RoutinesScreen(navController) }
        composable("workout/{routineId}") { backStack ->
            val id = backStack.arguments?.getString("routineId")?.toInt() ?: 1
            WorkoutScreen(navController, id)
        }
        composable("stats") { StatsScreen(navController) }

        // --- FLUJO COACH ---
        composable("coach_panel") {
            CoachHomeScreen(navController)
        }

        composable("coach_manage") {
            CoachManageRoutinesScreen(navController)
        }

        composable("create_routine") {
            CreateRoutineScreen(navController)
        }

        // --- NUEVA RUTA: AVANCES DE ALUMNOS (FIRESTORE) ---
        composable("students_progress") {
            StudentsProgressScreen(navController)
        }

        // --- PERFIL (COMPARTIDO) ---
        composable("profile") { ProfileScreen(navController) }
    }
}
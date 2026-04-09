package com.angel.proyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angel.proyectofinal.features.auth.presentation.screens.LoginScreen
import com.angel.proyectofinal.features.auth.presentation.screens.RegisterScreen
import com.angel.proyectofinal.features.coach.presentation.screens.CoachHomeScreen // Importamos la nueva pantalla
import com.angel.proyectofinal.features.coach.presentation.screens.CoachManageRoutinesScreen
import com.angel.proyectofinal.features.coach.presentation.screens.CreateRoutineScreen
import com.angel.proyectofinal.features.home.presentation.screens.HomeScreen
import com.angel.proyectofinal.features.profile.presentation.screens.ProfileScreen
import com.angel.proyectofinal.features.routines.presentation.screens.RoutinesScreen
import com.angel.proyectofinal.features.routines.presentation.screens.WorkoutScreen
import com.angel.proyectofinal.features.stats.presentation.screens.StatsScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
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

        // --- FLUJO COACH (TU SOLICITUD) ---
        // 1. Esta es la pantalla principal donde siempre entrará el Coach
        composable("coach_panel") {
            CoachHomeScreen(navController)
        }

        // 2. Pantalla para ver y borrar las rutinas (Gestionar)
        composable("coach_manage") {
            CoachManageRoutinesScreen(navController)
        }

        // 3. Pantalla para crear nuevas rutinas
        composable("create_routine") {
            CreateRoutineScreen(navController)
        }

        // --- PERFIL (COMPARTIDO) ---
        composable("profile") { ProfileScreen(navController) }
    }
}
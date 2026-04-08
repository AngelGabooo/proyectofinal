package com.angel.proyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angel.proyectofinal.features.auth.presentation.screens.LoginScreen
import com.angel.proyectofinal.features.auth.presentation.screens.RegisterScreen
import com.angel.proyectofinal.features.coach.presentation.screens.CoachManageRoutinesScreen
import com.angel.proyectofinal.features.coach.presentation.screens.CreateRoutineScreen
import com.angel.proyectofinal.features.home.presentation.screens.HomeScreen
import com.angel.proyectofinal.features.profile.presentation.screens.ProfileScreen
import com.angel.proyectofinal.features.routines.presentation.screens.RoutinesScreen
import com.angel.proyectofinal.features.routines.presentation.screens.WorkoutScreen
import com.angel.proyectofinal.features.stats.presentation.screens.StatsScreen

@Composable
fun AppNavHost(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        // --- AUTH ---
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        // --- ALUMNO ---
        composable("home") { HomeScreen(navController) }
        composable("routines") { RoutinesScreen(navController) }
        composable("workout/{routineId}") { backStack ->
            val id = backStack.arguments?.getString("routineId")?.toInt() ?: 1
            WorkoutScreen(navController, id)
        }
        composable("stats") { StatsScreen(navController) }

        // --- COACH (Aquí es donde estaba el error de cierre) ---
        composable("coach_panel") {
            CoachManageRoutinesScreen(navController)
        }

        composable("create_routine") {
            CreateRoutineScreen(navController)
        }

        // --- PERFIL ---
        composable("profile") { ProfileScreen(navController) }
    }
}
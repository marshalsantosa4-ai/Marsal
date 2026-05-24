package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.DashboardScreen

@Composable
fun PortalApp(viewModel: PortalViewModel) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("dashboard") {
            DashboardScreen(viewModel, navController)
        }
        composable("course_detail/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            com.example.ui.screens.CourseDetailScreen(courseId, viewModel, navController)
        }
        composable("materi/{moduleId}") { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
            com.example.ui.screens.MateriScreen(moduleId, viewModel, navController)
        }
        composable("quiz/{moduleId}") { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
            com.example.ui.screens.QuizScreen(moduleId, viewModel, navController)
        }
    }
}

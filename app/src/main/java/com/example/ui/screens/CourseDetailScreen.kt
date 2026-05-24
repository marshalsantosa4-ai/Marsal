package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ui.PortalViewModel
import com.example.data.StaticData
import com.example.data.Course
import com.example.data.CourseModule
import com.example.data.ModuleProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(courseId: String, viewModel: PortalViewModel, navController: NavController) {
    val course = StaticData.courses.find { it.id == courseId } ?: return
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    val modules = course.modules
    val courseProgresses = state.progresses.filter { it.courseId == courseId }
    val progressPercent = if (modules.isEmpty()) 0f else courseProgresses.count { it.isCompleted }.toFloat() / modules.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Mata Kuliah", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(course.color))
                    .padding(24.dp)
            ) {
                Column {
                    Text("${course.icon} ${course.name}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { progressPercent },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Text("${(progressPercent * 100).toInt()}% Selesai", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
            
            Text("Daftar Modul", fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
            
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                items(modules) { module ->
                    val moduleProgress = courseProgresses.find { it.moduleId == module.id }
                    val isDone = moduleProgress?.isCompleted == true
                    
                    ModuleCard(module, isDone, moduleProgress?.score ?: 0) {
                        navController.navigate("materi/${module.id}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ModuleCard(module: CourseModule, isDone: Boolean, score: Int, onClick: () -> Unit) {
    val borderColor = if (isDone) Color(0xFF16A34A) else MaterialTheme.colorScheme.outlineVariant
    val containerColor = if (isDone) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface
    val contentColor = if (isDone) Color(0xFF15803D) else MaterialTheme.colorScheme.onSurface
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(if (isDone) Color(0xFF16A34A) else Color.Transparent, RoundedCornerShape(12.dp))
                    .border(2.dp, if (isDone) Color(0xFF16A34A) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(module.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = contentColor)
            }
            if (isDone) {
                Badge(containerColor = Color(0xFF16A34A), contentColor = Color.White) {
                    Text("$score/100")
                }
            }
        }
    }
}

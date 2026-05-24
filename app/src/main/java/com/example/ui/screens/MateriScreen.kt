package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.PortalViewModel
import com.example.data.StaticData
import com.example.data.CourseModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriScreen(moduleId: String, viewModel: PortalViewModel, navController: NavController) {
    val module = StaticData.courses.flatMap { it.modules }.find { it.id == moduleId } ?: return
    val course = StaticData.courses.find { it.id == module.courseId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Badge(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White) {
                            Text(module.courseId.uppercase())
                        }
                        Text(module.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(16.dp)) {
                Button(
                    onClick = { 
                        navController.navigate("quiz/${module.id}")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Sudah Paham? Mulai Kuis")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(module.materials) { material ->
                MateriItem(material)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MateriItem(material: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B))
            Spacer(modifier = Modifier.width(12.dp))
            Text(material, fontSize = 14.sp)
        }
    }
}

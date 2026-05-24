package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.PortalViewModel
import com.example.data.StaticData
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(moduleId: String, viewModel: PortalViewModel, navController: NavController) {
    val module = StaticData.courses.flatMap { it.modules }.find { it.id == moduleId } ?: return
    val course = StaticData.courses.find { it.id == module.courseId } ?: return

    var currentQuestion by remember { mutableStateOf(1) }
    val totalQuestions = 5
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    
    val options = listOf("Option A", "Option B", "Option C", "Option D")
    val correctOption = 0 // Mock correct option is always A

    if (showResult) {
        QuizResultScreen(score = score, total = totalQuestions) {
            viewModel.completeModule(module.id, course.id, (score.toFloat() / totalQuestions * 100).toInt())
            navController.popBackStack("course_detail/${course.id}", inclusive = false)
        }
        return
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(course.name, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                LinearProgressIndicator(
                    progress = { currentQuestion.toFloat() / totalQuestions },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(16.dp)) {
                Button(
                    onClick = { 
                        if (!isSubmitted) {
                            isSubmitted = true
                            if (selectedOption == correctOption) score++
                        } else {
                            if (currentQuestion < totalQuestions) {
                                currentQuestion++
                                selectedOption = null
                                isSubmitted = false
                            } else {
                                showResult = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedOption != null,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isSubmitted) (if (currentQuestion < totalQuestions) "Selanjutnya" else "Selesai") else "Cek Jawaban")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Pertanyaan $currentQuestion dari $totalQuestions", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Apa konsep paling penting dalam ${module.title} yang telah kamu pelajari?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == index
                val isCorrect = index == correctOption
                val showCorrect = isSubmitted && isCorrect
                val showWrong = isSubmitted && isSelected && !isCorrect
                
                val containerColor = when {
                    showCorrect -> Color(0xFFF0FDF4)
                    showWrong -> Color(0xFFFEF2F2)
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }
                
                val borderColor = when {
                    showCorrect -> Color(0xFF16A34A)
                    showWrong -> Color(0xFFDC2626)
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outlineVariant
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isSubmitted) { selectedOption = index },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, borderColor),
                    colors = CardDefaults.cardColors(containerColor = containerColor)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(if (isSelected || showCorrect || showWrong) MaterialTheme.colorScheme.primary else Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(('A' + index).toString(), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(option, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResultScreen(score: Int, total: Int, onFinish: () -> Unit) {
    val percentage = (score.toFloat() / total * 100).toInt()
    
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🏆", fontSize = 64.sp)
        Text("$percentage%", fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Text("Skor Kamu", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) {
            Text("Selesai & Lanjut")
        }
    }
}

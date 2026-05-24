package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.Course
import com.example.data.ModuleProgress
import com.example.data.ProgressRepository
import com.example.data.StaticData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardState(
    val courses: List<Course> = StaticData.courses,
    val progresses: List<ModuleProgress> = emptyList(),
    val streakDays: Int = 0,
    val searchQuery: String = "",
    val filterType: String = "all" // all, progress, new, done
)

class PortalViewModel(private val repository: ProgressRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _filterType = MutableStateFlow("all")
    
    val uiState: StateFlow<DashboardState> = combine(
        repository.allProgress,
        _searchQuery,
        _filterType
    ) { progresses, search, filter ->
        val filteredCourses = StaticData.courses.filter { 
            it.name.contains(search, ignoreCase = true) || it.code.contains(search, ignoreCase = true)
        }.filter { course ->
            val courseProgresses = progresses.filter { it.courseId == course.id }
            when(filter) {
                "progress" -> courseProgresses.isNotEmpty() && !courseProgresses.all { it.isCompleted }
                "new" -> courseProgresses.isEmpty()
                "done" -> courseProgresses.isNotEmpty() && courseProgresses.size == course.modules.size && courseProgresses.all { it.isCompleted }
                else -> true
            }
        }
        
        DashboardState(
            courses = filteredCourses,
            progresses = progresses,
            streakDays = 3, // mock streak
            searchQuery = search,
            filterType = filter
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: String) {
        _filterType.value = filter
    }

    fun completeModule(moduleId: String, courseId: String, score: Int) {
        viewModelScope.launch {
            repository.saveProgress(
                ModuleProgress(
                    moduleId = moduleId,
                    courseId = courseId,
                    isCompleted = true,
                    score = score
                )
            )
        }
    }
}

class PortalViewModelFactory(private val repository: ProgressRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PortalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "module_progress")
data class ModuleProgress(
    @PrimaryKey val moduleId: String, 
    val courseId: String,
    val isCompleted: Boolean = false,
    val score: Int = 0
)

@Dao
interface ProgressDao {
    @Query("SELECT * FROM module_progress")
    fun getAllProgress(): Flow<List<ModuleProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ModuleProgress)
    
    @Query("SELECT * FROM module_progress WHERE courseId = :courseId")
    fun getProgressForCourse(courseId: String): Flow<List<ModuleProgress>>
    
    @Query("SELECT * FROM module_progress WHERE moduleId = :moduleId")
    suspend fun getModuleProgress(moduleId: String): ModuleProgress?
}

@Database(entities = [ModuleProgress::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
}

class ProgressRepository(private val progressDao: ProgressDao) {
    val allProgress: Flow<List<ModuleProgress>> = progressDao.getAllProgress()
    
    fun getProgressForCourse(courseId: String) = progressDao.getProgressForCourse(courseId)
    
    suspend fun saveProgress(progress: ModuleProgress) {
        progressDao.insertProgress(progress)
    }
}

package com.rmmc.studenttaskhub.data.local

import androidx.room.*
import com.rmmc.studenttaskhub.data.model.TaskSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskSchedule)

    @Update
    suspend fun updateTask(task: TaskSchedule)

    @Delete
    suspend fun deleteTask(task: TaskSchedule)

    @Query("SELECT * FROM task_schedules ORDER BY dateTimeMillis ASC")
    fun getAllTasks(): Flow<List<TaskSchedule>>

    @Query("SELECT * FROM task_schedules WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskSchedule?
}

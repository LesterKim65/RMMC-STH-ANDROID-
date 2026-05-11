package com.rmmc.studenttaskhub.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rmmc.studenttaskhub.data.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY deadlineMillis ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE deadlineMillis >= :start AND deadlineMillis <= :end ORDER BY deadlineMillis ASC")
    fun getTasksInRange(start: Long, end: Long): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE deadlineMillis >= :now ORDER BY deadlineMillis ASC LIMIT :limit")
    fun getUpcomingTasks(now: Long, limit: Int = 5): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE reminderMillis IS NOT NULL")
    suspend fun getTasksWithReminders(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}

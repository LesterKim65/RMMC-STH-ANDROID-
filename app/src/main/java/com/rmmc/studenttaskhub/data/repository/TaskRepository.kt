package com.rmmc.studenttaskhub.data.repository

import androidx.lifecycle.LiveData
import com.rmmc.studenttaskhub.data.local.TaskDao
import com.rmmc.studenttaskhub.data.model.Task

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun getTodayTasks(start: Long, end: Long): LiveData<List<Task>> = taskDao.getTasksInRange(start, end)

    fun getUpcomingTasks(now: Long, limit: Int = 5): LiveData<List<Task>> = taskDao.getUpcomingTasks(now, limit)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}

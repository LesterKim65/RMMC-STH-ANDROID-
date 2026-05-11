package com.rmmc.studenttaskhub.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rmmc.studenttaskhub.data.model.Task
import com.rmmc.studenttaskhub.data.repository.TaskRepository
import com.rmmc.studenttaskhub.ui.common.DateTimeUtils

class DashboardViewModel(
    taskRepository: TaskRepository
) : ViewModel() {
    val allTasks: LiveData<List<Task>> = taskRepository.getAllTasks()

    private val todayRange = DateTimeUtils.todayRange()
    val todayTasks: LiveData<List<Task>> = taskRepository.getTodayTasks(todayRange.first, todayRange.second)

    val upcomingTasks: LiveData<List<Task>> = taskRepository.getUpcomingTasks(System.currentTimeMillis())
}

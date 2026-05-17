package com.rmmc.studenttaskhub.ui.dashboard

import androidx.lifecycle.*
import com.rmmc.studenttaskhub.data.model.Schedule
import com.rmmc.studenttaskhub.data.model.Task
import com.rmmc.studenttaskhub.data.model.TaskStatus
import com.rmmc.studenttaskhub.data.repository.ScheduleRepository
import com.rmmc.studenttaskhub.data.repository.TaskRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(
    private val taskRepository: TaskRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    val upcomingTasks: LiveData<List<Task>> = taskRepository.getAllTasks().map { tasks ->
        tasks.filter { it.status == TaskStatus.PENDING }
            .sortedBy { it.deadlineMillis }
            .take(5)
    }

    val todaySchedules: LiveData<List<Schedule>> = scheduleRepository.getAllSchedules().map { schedules ->
        val today = getCurrentDay()
        schedules.filter { it.day.name == today }
            .sortedBy { it.startTimeMillis }
    }

    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            val newStatus = if (isCompleted) TaskStatus.COMPLETED else TaskStatus.PENDING
            taskRepository.updateTask(task.copy(status = newStatus))
        }
    }

    private fun getCurrentDay(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "MONDAY"
            Calendar.TUESDAY -> "TUESDAY"
            Calendar.WEDNESDAY -> "WEDNESDAY"
            Calendar.THURSDAY -> "THURSDAY"
            Calendar.FRIDAY -> "FRIDAY"
            Calendar.SATURDAY -> "SATURDAY"
            Calendar.SUNDAY -> "SUNDAY"
            else -> "MONDAY"
        }
    }
}

class DashboardViewModelFactory(
    private val taskRepository: TaskRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(taskRepository, scheduleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

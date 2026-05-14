package com.rmmc.studenttaskhub.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rmmc.studenttaskhub.StudentTaskHubApp
import com.rmmc.studenttaskhub.ui.schedule.ScheduleViewModel
import com.rmmc.studenttaskhub.ui.tasks.TaskListViewModel

class AppViewModelFactory(private val app: StudentTaskHubApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TaskListViewModel::class.java) -> {
                TaskListViewModel(app, app.container.taskRepository) as T
            }

            modelClass.isAssignableFrom(ScheduleViewModel::class.java) -> {
                ScheduleViewModel(app, app.container.scheduleRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

package com.rmmc.studenttaskhub.ui.common

import android.content.Context
import com.rmmc.studenttaskhub.data.local.AppDatabase
import com.rmmc.studenttaskhub.data.repository.ScheduleRepository
import com.rmmc.studenttaskhub.data.repository.TaskRepository

class AppContainer(context: Context) {
    private val database = AppDatabase.getInstance(context)

    val taskRepository = TaskRepository(database.taskDao())
    val scheduleRepository = ScheduleRepository(database.scheduleDao())
}

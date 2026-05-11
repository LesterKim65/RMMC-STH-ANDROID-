package com.rmmc.studenttaskhub.ui.schedule

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmmc.studenttaskhub.data.model.Schedule
import com.rmmc.studenttaskhub.data.repository.ScheduleRepository
import com.rmmc.studenttaskhub.notifications.ReminderScheduler
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val app: Application,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    val schedules = scheduleRepository.getAllSchedules()

    fun upsertSchedule(schedule: Schedule) {
        viewModelScope.launch {
            if (schedule.id == 0) {
                val id = scheduleRepository.insertSchedule(schedule).toInt()
                ReminderScheduler.scheduleClassReminder(app, schedule.copy(id = id))
            } else {
                scheduleRepository.updateSchedule(schedule)
                ReminderScheduler.cancelClassReminder(app, schedule.id)
                ReminderScheduler.scheduleClassReminder(app, schedule)
            }
        }
    }

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(schedule)
            ReminderScheduler.cancelClassReminder(app, schedule.id)
        }
    }
}

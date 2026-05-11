package com.rmmc.studenttaskhub.data.repository

import androidx.lifecycle.LiveData
import com.rmmc.studenttaskhub.data.local.ScheduleDao
import com.rmmc.studenttaskhub.data.model.Schedule

class ScheduleRepository(private val scheduleDao: ScheduleDao) {
    fun getAllSchedules(): LiveData<List<Schedule>> = scheduleDao.getAllSchedules()

    suspend fun insertSchedule(schedule: Schedule): Long = scheduleDao.insertSchedule(schedule)

    suspend fun updateSchedule(schedule: Schedule) = scheduleDao.updateSchedule(schedule)

    suspend fun deleteSchedule(schedule: Schedule) = scheduleDao.deleteSchedule(schedule)
}

package com.rmmc.studenttaskhub.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rmmc.studenttaskhub.data.model.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules ORDER BY day ASC, startTimeMillis ASC")
    fun getAllSchedules(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE classStartReminderMillis IS NOT NULL")
    suspend fun getSchedulesWithReminders(): List<Schedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule): Long

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
}

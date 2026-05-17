package com.rmmc.studenttaskhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

enum class AlarmFrequency {
    ONCE, DAILY, WEEKLY
}

@Entity(tableName = "schedules")
@Parcelize
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val instructor: String,
    val room: String,
    val day: WeekDay,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val classStartReminderMillis: Long? = null,
    val alarmFrequency: AlarmFrequency = AlarmFrequency.WEEKLY,
    val alarmSound: String? = null
) : Parcelable

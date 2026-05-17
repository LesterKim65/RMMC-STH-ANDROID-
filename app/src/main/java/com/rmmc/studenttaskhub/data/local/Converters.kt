package com.rmmc.studenttaskhub.data.local

import androidx.room.TypeConverter
import com.rmmc.studenttaskhub.data.model.*

class Converters {
    @TypeConverter
    fun fromTaskPriority(value: TaskPriority): String = value.name

    @TypeConverter
    fun toTaskPriority(value: String?): TaskPriority {
        if (value == null) return TaskPriority.MEDIUM
        return try {
            TaskPriority.valueOf(value)
        } catch (e: Exception) {
            TaskPriority.MEDIUM
        }
    }

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String?): TaskStatus {
        if (value == null) return TaskStatus.PENDING
        return try {
            TaskStatus.valueOf(value)
        } catch (e: Exception) {
            TaskStatus.PENDING
        }
    }

    @TypeConverter
    fun fromTaskType(value: TaskType): String = value.name

    @TypeConverter
    fun toTaskType(value: String?): TaskType {
        if (value == null) return TaskType.ASSIGNMENT
        return try {
            TaskType.valueOf(value)
        } catch (e: Exception) {
            TaskType.ASSIGNMENT
        }
    }

    @TypeConverter
    fun fromWeekDay(value: WeekDay): String = value.name

    @TypeConverter
    fun toWeekDay(value: String?): WeekDay {
        if (value == null) return WeekDay.MONDAY
        return try {
            WeekDay.valueOf(value)
        } catch (e: Exception) {
            WeekDay.MONDAY
        }
    }

    @TypeConverter
    fun fromAlarmFrequency(value: AlarmFrequency): String = value.name

    @TypeConverter
    fun toAlarmFrequency(value: String?): AlarmFrequency {
        if (value == null) return AlarmFrequency.WEEKLY
        return try {
            AlarmFrequency.valueOf(value)
        } catch (e: Exception) {
            AlarmFrequency.WEEKLY
        }
    }
}

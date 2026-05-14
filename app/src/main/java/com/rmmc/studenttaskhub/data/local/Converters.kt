package com.rmmc.studenttaskhub.data.local

import androidx.room.TypeConverter
import com.rmmc.studenttaskhub.data.model.TaskPriority
import com.rmmc.studenttaskhub.data.model.TaskStatus
import com.rmmc.studenttaskhub.data.model.TaskType
import com.rmmc.studenttaskhub.data.model.WeekDay

class Converters {
    @TypeConverter
    fun fromTaskPriority(value: TaskPriority): String = value.name

    @TypeConverter
    fun toTaskPriority(value: String): TaskPriority = TaskPriority.valueOf(value)

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus = TaskStatus.valueOf(value)

    @TypeConverter
    fun fromTaskType(value: TaskType): String = value.name

    @TypeConverter
    fun toTaskType(value: String): TaskType = TaskType.valueOf(value)

    @TypeConverter
    fun fromWeekDay(value: WeekDay): String = value.name

    @TypeConverter
    fun toWeekDay(value: String): WeekDay = WeekDay.valueOf(value)
}

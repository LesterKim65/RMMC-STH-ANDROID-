package com.rmmc.studenttaskhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_schedules")
data class TaskSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val dateTimeMillis: Long,
    val type: TaskType,
    val isCompleted: Boolean = false
)

enum class TaskType {
    CLASS,
    ASSIGNMENT
}

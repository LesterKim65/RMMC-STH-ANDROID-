package com.rmmc.studenttaskhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

enum class TaskPriority { LOW, MEDIUM, HIGH }
enum class TaskStatus { PENDING, COMPLETED }

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subjectName: String,
    val description: String,
    val deadlineMillis: Long,
    val priority: TaskPriority,
    val status: TaskStatus = TaskStatus.PENDING,
    val reminderMillis: Long? = null
) : Parcelable

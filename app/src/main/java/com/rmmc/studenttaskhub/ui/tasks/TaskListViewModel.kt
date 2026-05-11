package com.rmmc.studenttaskhub.ui.tasks

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmmc.studenttaskhub.data.model.Task
import com.rmmc.studenttaskhub.data.model.TaskPriority
import com.rmmc.studenttaskhub.data.model.TaskStatus
import com.rmmc.studenttaskhub.data.repository.TaskRepository
import com.rmmc.studenttaskhub.notifications.ReminderScheduler
import kotlinx.coroutines.launch

enum class TaskSortOption { DEADLINE, PRIORITY }
enum class TaskFilterStatus { ALL, PENDING, COMPLETED }

class TaskListViewModel(
    private val app: Application,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val allTasks = taskRepository.getAllTasks()
    private val searchQuery = MutableLiveData("")
    private val filterStatus = MutableLiveData(TaskFilterStatus.ALL)
    private val filterPriority = MutableLiveData<TaskPriority?>(null)
    private val sortOption = MutableLiveData(TaskSortOption.DEADLINE)

    val tasks: LiveData<List<Task>> = MediatorLiveData<List<Task>>().apply {
        fun refresh() {
            value = applyFilters(
                base = allTasks.value.orEmpty(),
                query = searchQuery.value.orEmpty(),
                statusFilter = filterStatus.value ?: TaskFilterStatus.ALL,
                priorityFilter = filterPriority.value,
                sort = sortOption.value ?: TaskSortOption.DEADLINE
            )
        }

        addSource(allTasks) { refresh() }
        addSource(searchQuery) { refresh() }
        addSource(filterStatus) { refresh() }
        addSource(filterPriority) { refresh() }
        addSource(sortOption) { refresh() }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setFilterStatus(status: TaskFilterStatus) {
        filterStatus.value = status
    }

    fun setFilterPriority(priority: TaskPriority?) {
        filterPriority.value = priority
    }

    fun setSortOption(option: TaskSortOption) {
        sortOption.value = option
    }

    fun upsertTask(task: Task) {
        viewModelScope.launch {
            if (task.id == 0) {
                val id = taskRepository.insertTask(task).toInt()
                ReminderScheduler.scheduleTaskReminder(app, task.copy(id = id))
            } else {
                taskRepository.updateTask(task)
                ReminderScheduler.cancelTaskReminder(app, task.id)
                ReminderScheduler.scheduleTaskReminder(app, task)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            ReminderScheduler.cancelTaskReminder(app, task.id)
        }
    }

    fun toggleTaskStatus(task: Task, completed: Boolean) {
        val updated = task.copy(status = if (completed) TaskStatus.COMPLETED else TaskStatus.PENDING)
        upsertTask(updated)
    }

    private fun applyFilters(
        base: List<Task>,
        query: String,
        statusFilter: TaskFilterStatus,
        priorityFilter: TaskPriority?,
        sort: TaskSortOption
    ): List<Task> {
        val filtered = base.filter { task ->
            val matchesQuery =
                task.title.contains(query, true) ||
                    task.subjectName.contains(query, true) ||
                    task.description.contains(query, true)

            val matchesStatus = when (statusFilter) {
                TaskFilterStatus.ALL -> true
                TaskFilterStatus.PENDING -> task.status == TaskStatus.PENDING
                TaskFilterStatus.COMPLETED -> task.status == TaskStatus.COMPLETED
            }

            val matchesPriority = priorityFilter?.let { task.priority == it } ?: true
            matchesQuery && matchesStatus && matchesPriority
        }

        return when (sort) {
            TaskSortOption.DEADLINE -> filtered.sortedBy { it.deadlineMillis }
            TaskSortOption.PRIORITY -> filtered.sortedBy {
                when (it.priority) {
                    TaskPriority.HIGH -> 0
                    TaskPriority.MEDIUM -> 1
                    TaskPriority.LOW -> 2
                }
            }
        }
    }
}

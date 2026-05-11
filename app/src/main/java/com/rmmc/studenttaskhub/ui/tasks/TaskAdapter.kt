package com.rmmc.studenttaskhub.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rmmc.studenttaskhub.data.model.Task
import com.rmmc.studenttaskhub.data.model.TaskStatus
import com.rmmc.studenttaskhub.databinding.ItemTaskBinding
import com.rmmc.studenttaskhub.ui.common.DateTimeUtils

class TaskAdapter(
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit,
    private val onStatusChanged: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskSubject.text = task.subjectName
            binding.taskDescription.text = task.description
            binding.taskDeadline.text = DateTimeUtils.formatDateTime(task.deadlineMillis)
            binding.taskPriority.text = task.priority.name
            binding.taskStatusChip.text = task.status.name
            binding.taskCompletedCheckbox.setOnCheckedChangeListener(null)
            binding.taskCompletedCheckbox.isChecked = task.status == TaskStatus.COMPLETED

            binding.taskCompletedCheckbox.setOnCheckedChangeListener { _, checked ->
                onStatusChanged(task, checked)
            }

            binding.editTaskButton.setOnClickListener { onEdit(task) }
            binding.deleteTaskButton.setOnClickListener { onDelete(task) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    }
}

package com.rmmc.studenttaskhub.ui.tasks

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.rmmc.studenttaskhub.R
import com.rmmc.studenttaskhub.data.model.Task
import com.rmmc.studenttaskhub.data.model.TaskPriority
import com.rmmc.studenttaskhub.data.model.TaskStatus
import com.rmmc.studenttaskhub.databinding.DialogTaskEditBinding
import java.util.Calendar

class TaskEditDialogFragment : DialogFragment() {
    private var _binding: DialogTaskEditBinding? = null
    private val binding get() = _binding!!

    private val existingTask: Task? by lazy {
        arguments?.getParcelable(ARG_TASK)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogTaskEditBinding.inflate(layoutInflater)

        val calendar = Calendar.getInstance()
        var selectedDeadline = existingTask?.deadlineMillis ?: System.currentTimeMillis()

        existingTask?.let { task ->
            binding.taskTitleInput.setText(task.title)
            binding.taskSubjectInput.setText(task.subjectName)
            binding.taskDescriptionInput.setText(task.description)
            binding.priorityDropdown.setText(task.priority.name, false)
            binding.statusDropdown.setText(task.status.name, false)
        }

        if (existingTask == null) {
            binding.priorityDropdown.setText(TaskPriority.MEDIUM.name, false)
            binding.statusDropdown.setText(TaskStatus.PENDING.name, false)
        }

        binding.deadlineInput.setText(com.rmmc.studenttaskhub.ui.common.DateTimeUtils.formatDateTime(selectedDeadline))

        binding.deadlineInput.setOnClickListener {
            calendar.timeInMillis = selectedDeadline
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    TimePickerDialog(
                        requireContext(),
                        { _, hour, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)
                            selectedDeadline = calendar.timeInMillis
                            binding.deadlineInput.setText(
                                com.rmmc.studenttaskhub.ui.common.DateTimeUtils.formatDateTime(selectedDeadline)
                            )
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val priorityAdapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            TaskPriority.entries.map { it.name }
        )
        binding.priorityDropdown.setAdapter(priorityAdapter)

        val statusAdapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            TaskStatus.entries.map { it.name }
        )
        binding.statusDropdown.setAdapter(statusAdapter)

        return AlertDialog.Builder(requireContext())
            .setTitle(if (existingTask == null) R.string.add_task else R.string.edit_task)
            .setView(binding.root)
            .setPositiveButton(R.string.save) { _, _
                ->
                val title = binding.taskTitleInput.text.toString().trim()
                val subject = binding.taskSubjectInput.text.toString().trim()
                val description = binding.taskDescriptionInput.text.toString().trim()

                if (title.isEmpty() || subject.isEmpty()) return@setPositiveButton

                val priorityText = binding.priorityDropdown.text.toString().ifBlank { TaskPriority.MEDIUM.name }
                val statusText = binding.statusDropdown.text.toString().ifBlank { TaskStatus.PENDING.name }
                val priority = TaskPriority.valueOf(priorityText)
                val status = TaskStatus.valueOf(statusText)
                val reminderMillis = selectedDeadline - 30 * 60 * 1000

                val task = Task(
                    id = existingTask?.id ?: 0,
                    title = title,
                    subjectName = subject,
                    description = description,
                    deadlineMillis = selectedDeadline,
                    priority = priority,
                    status = status,
                    reminderMillis = reminderMillis
                )
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(BUNDLE_TASK to task)
                )
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val REQUEST_KEY = "task_edit_request"
        const val BUNDLE_TASK = "bundle_task"
        private const val ARG_TASK = "arg_task"

        fun newInstance(task: Task? = null): TaskEditDialogFragment {
            return TaskEditDialogFragment().apply {
                arguments = bundleOf(ARG_TASK to task)
            }
        }
    }
}

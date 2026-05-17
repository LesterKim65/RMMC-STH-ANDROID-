package com.rmmc.studenttaskhub.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmmc.studenttaskhub.data.local.AppDatabase
import com.rmmc.studenttaskhub.data.model.Task
import com.rmmc.studenttaskhub.data.repository.ScheduleRepository
import com.rmmc.studenttaskhub.data.repository.TaskRepository
import com.rmmc.studenttaskhub.databinding.FragmentDashboardBinding
import com.rmmc.studenttaskhub.ui.schedule.ScheduleAdapter
import com.rmmc.studenttaskhub.ui.tasks.TaskAdapter

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        DashboardViewModelFactory(
            TaskRepository(database.taskDao()),
            ScheduleRepository(database.scheduleDao())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleAdapter = ScheduleAdapter({}, {})
        binding.todayScheduleRecyclerView.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val taskAdapter = TaskAdapter({}, {}, { task, isCompleted ->
            viewModel.updateTaskStatus(task, isCompleted)
        })
        binding.upcomingTasksRecyclerView.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.todaySchedules.observe(viewLifecycleOwner) { schedules ->
            scheduleAdapter.submitList(schedules)
            binding.emptyScheduleText.visibility = if (schedules.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.upcomingTasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)
            binding.emptyTasksText.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

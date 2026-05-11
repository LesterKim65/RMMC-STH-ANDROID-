package com.rmmc.studenttaskhub.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rmmc.studenttaskhub.R
import com.rmmc.studenttaskhub.StudentTaskHubApp
import com.rmmc.studenttaskhub.data.model.TaskStatus
import com.rmmc.studenttaskhub.databinding.FragmentDashboardBinding
import com.rmmc.studenttaskhub.ui.common.AppViewModelFactory
import com.rmmc.studenttaskhub.ui.MainActivity

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels {
        AppViewModelFactory(requireActivity().application as StudentTaskHubApp)
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

        viewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            val total = tasks.size
            val completed = tasks.count { it.status == TaskStatus.COMPLETED }
            val pending = tasks.count { it.status == TaskStatus.PENDING }
            binding.totalTasksValue.text = total.toString()
            binding.completedTasksValue.text = completed.toString()
            binding.pendingTasksValue.text = pending.toString()
        }

        viewModel.todayTasks.observe(viewLifecycleOwner) { tasks ->
            binding.todayTasksValue.text = tasks.size.toString()
        }

        viewModel.upcomingTasks.observe(viewLifecycleOwner) { tasks ->
            val nearest = tasks.firstOrNull()
            binding.nextDeadlineValue.text = nearest?.title ?: getString(R.string.no_upcoming_tasks)
        }

        binding.addTaskButton.setOnClickListener {
            (requireActivity() as MainActivity).navigateToTasks()
        }

        binding.viewScheduleButton.setOnClickListener {
            (requireActivity() as MainActivity).navigateToSchedule()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

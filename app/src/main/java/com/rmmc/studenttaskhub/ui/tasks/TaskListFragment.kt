package com.rmmc.studenttaskhub.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmmc.studenttaskhub.StudentTaskHubApp
import com.rmmc.studenttaskhub.data.model.Task
import com.rmmc.studenttaskhub.data.model.TaskPriority
import com.rmmc.studenttaskhub.databinding.FragmentTaskListBinding
import com.rmmc.studenttaskhub.ui.common.AppViewModelFactory

class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskListViewModel by viewModels {
        AppViewModelFactory(requireActivity().application as StudentTaskHubApp)
    }

    private val adapter by lazy {
        TaskAdapter(
            onEdit = { openDialog(it) },
            onDelete = { viewModel.deleteTask(it) },
            onStatusChanged = { task, checked -> viewModel.toggleTaskStatus(task, checked) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.searchInput.doAfterTextChanged {
            viewModel.setSearchQuery(it?.toString().orEmpty())
        }

        val statusValues = listOf("ALL", "PENDING", "COMPLETED")
        binding.statusFilterDropdown.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, statusValues)
        )
        binding.statusFilterDropdown.setText(statusValues.first(), false)
        binding.statusFilterDropdown.setOnItemClickListener { _, _, position, _ ->
            viewModel.setFilterStatus(TaskFilterStatus.valueOf(statusValues[position]))
        }

        val priorityValues = listOf("ALL", "LOW", "MEDIUM", "HIGH")
        binding.priorityFilterDropdown.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, priorityValues)
        )
        binding.priorityFilterDropdown.setText(priorityValues.first(), false)
        binding.priorityFilterDropdown.setOnItemClickListener { _, _, position, _ ->
            val selected = priorityValues[position]
            viewModel.setFilterPriority(if (selected == "ALL") null else TaskPriority.valueOf(selected))
        }

        val sortValues = listOf("DEADLINE", "PRIORITY")
        binding.sortDropdown.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sortValues)
        )
        binding.sortDropdown.setText(sortValues.first(), false)
        binding.sortDropdown.setOnItemClickListener { _, _, position, _ ->
            viewModel.setSortOption(TaskSortOption.valueOf(sortValues[position]))
        }

        binding.addTaskFab.setOnClickListener {
            openDialog(null)
        }

        setFragmentResultListener(TaskEditDialogFragment.REQUEST_KEY) { _, bundle ->
            val task = bundle.getParcelable<Task>(TaskEditDialogFragment.BUNDLE_TASK) ?: return@setFragmentResultListener
            viewModel.upsertTask(task)
        }
    }

    private fun openDialog(task: Task?) {
        TaskEditDialogFragment.newInstance(task).show(parentFragmentManager, "TaskEditDialog")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

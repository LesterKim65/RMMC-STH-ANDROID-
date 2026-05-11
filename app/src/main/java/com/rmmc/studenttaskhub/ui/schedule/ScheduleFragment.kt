package com.rmmc.studenttaskhub.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmmc.studenttaskhub.StudentTaskHubApp
import com.rmmc.studenttaskhub.data.model.Schedule
import com.rmmc.studenttaskhub.databinding.FragmentScheduleBinding
import com.rmmc.studenttaskhub.ui.common.AppViewModelFactory

class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by viewModels {
        AppViewModelFactory(requireActivity().application as StudentTaskHubApp)
    }

    private val adapter by lazy {
        ScheduleAdapter(
            onEdit = { openDialog(it) },
            onDelete = { viewModel.deleteSchedule(it) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.scheduleRecyclerView.adapter = adapter

        viewModel.schedules.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.addScheduleFab.setOnClickListener { openDialog(null) }

        setFragmentResultListener(ScheduleEditDialogFragment.REQUEST_KEY) { _, bundle ->
            val schedule = bundle.getParcelable<Schedule>(ScheduleEditDialogFragment.BUNDLE_SCHEDULE)
                ?: return@setFragmentResultListener
            viewModel.upsertSchedule(schedule)
        }
    }

    private fun openDialog(schedule: Schedule?) {
        ScheduleEditDialogFragment.newInstance(schedule)
            .show(parentFragmentManager, "ScheduleEditDialog")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

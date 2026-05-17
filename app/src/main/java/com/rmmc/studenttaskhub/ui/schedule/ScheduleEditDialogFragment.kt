package com.rmmc.studenttaskhub.ui.schedule

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.rmmc.studenttaskhub.R
import com.rmmc.studenttaskhub.data.model.Schedule
import com.rmmc.studenttaskhub.data.model.WeekDay
import com.rmmc.studenttaskhub.databinding.DialogScheduleEditBinding
import com.rmmc.studenttaskhub.ui.common.DateTimeUtils
import java.util.Calendar

class ScheduleEditDialogFragment : DialogFragment() {
    private var _binding: DialogScheduleEditBinding? = null
    private val binding get() = _binding!!

    private val existing: Schedule? by lazy {
        arguments?.getParcelable(ARG_SCHEDULE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogScheduleEditBinding.inflate(layoutInflater)

        val startCalendar = Calendar.getInstance().apply {
            timeInMillis = existing?.startTimeMillis ?: System.currentTimeMillis()
        }
        val endCalendar = Calendar.getInstance().apply {
            timeInMillis = existing?.endTimeMillis ?: System.currentTimeMillis() + 60 * 60 * 1000
        }

        existing?.let {
            binding.subjectInput.setText(it.subject)
            binding.instructorInput.setText(it.instructor)
            binding.roomInput.setText(it.room)
            binding.dayDropdown.setText(it.day.name, false)
            binding.frequencyDropdown.setText(it.alarmFrequency.name, false)
        }

        binding.startTimeInput.setText(DateTimeUtils.formatTime(startCalendar.timeInMillis))
        binding.endTimeInput.setText(DateTimeUtils.formatTime(endCalendar.timeInMillis))

        val daysAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            WeekDay.entries.map { it.name }
        )
        binding.dayDropdown.setAdapter(daysAdapter)

        val freqAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            com.rmmc.studenttaskhub.data.model.AlarmFrequency.entries.map { it.name }
        )
        binding.frequencyDropdown.setAdapter(freqAdapter)

        if (existing == null) {
            binding.dayDropdown.setText(WeekDay.MONDAY.name, false)
            binding.frequencyDropdown.setText(com.rmmc.studenttaskhub.data.model.AlarmFrequency.WEEKLY.name, false)
        }

        binding.startTimeInput.setOnClickListener {
            showTimePicker(startCalendar) {
                binding.startTimeInput.setText(DateTimeUtils.formatTime(it))
            }
        }

        binding.endTimeInput.setOnClickListener {
            showTimePicker(endCalendar) {
                binding.endTimeInput.setText(DateTimeUtils.formatTime(it))
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(if (existing == null) R.string.add_schedule else R.string.edit_schedule)
            .setView(binding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                val subject = binding.subjectInput.text.toString().trim()
                val instructor = binding.instructorInput.text.toString().trim()
                val room = binding.roomInput.text.toString().trim()
                if (subject.isEmpty()) return@setPositiveButton

                val day = WeekDay.valueOf(binding.dayDropdown.text.toString())
                val frequency = com.rmmc.studenttaskhub.data.model.AlarmFrequency.valueOf(binding.frequencyDropdown.text.toString())
                val nextStart = nextOccurrence(
                    day = day,
                    hour = startCalendar.get(Calendar.HOUR_OF_DAY),
                    minute = startCalendar.get(Calendar.MINUTE)
                )
                val nextEnd = nextOccurrence(
                    day = day,
                    hour = endCalendar.get(Calendar.HOUR_OF_DAY),
                    minute = endCalendar.get(Calendar.MINUTE)
                ).let { if (it <= nextStart) it + 60 * 60 * 1000 else it }

                val classStartReminderMillis = nextStart - 30 * 60 * 1000

                val schedule = Schedule(
                    id = existing?.id ?: 0,
                    subject = subject,
                    instructor = instructor,
                    room = room,
                    day = day,
                    startTimeMillis = nextStart,
                    endTimeMillis = nextEnd,
                    classStartReminderMillis = classStartReminderMillis,
                    alarmFrequency = frequency
                )

                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(BUNDLE_SCHEDULE to schedule)
                )
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun showTimePicker(calendar: Calendar, onTimeSelected: (Long) -> Unit) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText("Select Time")
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .build()

        picker.addOnPositiveButtonClickListener {
            calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
            calendar.set(Calendar.MINUTE, picker.minute)
            onTimeSelected(calendar.timeInMillis)
        }
        picker.show(childFragmentManager, "time_picker")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val REQUEST_KEY = "schedule_edit_request"
        const val BUNDLE_SCHEDULE = "bundle_schedule"
        private const val ARG_SCHEDULE = "arg_schedule"

        fun newInstance(schedule: Schedule? = null): ScheduleEditDialogFragment {
            return ScheduleEditDialogFragment().apply {
                arguments = bundleOf(ARG_SCHEDULE to schedule)
            }
        }
    }

    private fun nextOccurrence(day: WeekDay, hour: Int, minute: Int): Long {
        val targetDay = when (day) {
            WeekDay.SUNDAY -> Calendar.SUNDAY
            WeekDay.MONDAY -> Calendar.MONDAY
            WeekDay.TUESDAY -> Calendar.TUESDAY
            WeekDay.WEDNESDAY -> Calendar.WEDNESDAY
            WeekDay.THURSDAY -> Calendar.THURSDAY
            WeekDay.FRIDAY -> Calendar.FRIDAY
            WeekDay.SATURDAY -> Calendar.SATURDAY
        }

        val cal = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        while (cal.get(Calendar.DAY_OF_WEEK) != targetDay || cal.timeInMillis <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return cal.timeInMillis
    }
}

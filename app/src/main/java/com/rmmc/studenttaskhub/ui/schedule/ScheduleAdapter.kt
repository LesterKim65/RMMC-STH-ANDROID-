package com.rmmc.studenttaskhub.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rmmc.studenttaskhub.data.model.Schedule
import com.rmmc.studenttaskhub.databinding.ItemScheduleBinding
import com.rmmc.studenttaskhub.ui.common.DateTimeUtils

class ScheduleAdapter(
    private val onEdit: ((Schedule) -> Unit)? = null,
    private val onDelete: ((Schedule) -> Unit)? = null
) : ListAdapter<Schedule, ScheduleAdapter.ScheduleViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ScheduleViewHolder(private val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Schedule) {
            binding.scheduleSubject.text = item.subject
            binding.scheduleInstructor.text = item.instructor
            binding.scheduleRoom.text = item.room
            binding.scheduleDay.text = item.day.name
            binding.scheduleTime.text = binding.root.context.getString(
                com.rmmc.studenttaskhub.R.string.schedule_time_range,
                DateTimeUtils.formatTime(item.startTimeMillis),
                DateTimeUtils.formatTime(item.endTimeMillis)
            )

            val colors = listOf(
                com.rmmc.studenttaskhub.R.color.sch_color_1,
                com.rmmc.studenttaskhub.R.color.sch_color_2,
                com.rmmc.studenttaskhub.R.color.sch_color_3,
                com.rmmc.studenttaskhub.R.color.sch_color_4,
                com.rmmc.studenttaskhub.R.color.sch_color_5,
                com.rmmc.studenttaskhub.R.color.sch_color_6
            )
            val colorRes = colors[item.id % colors.size]
            binding.scheduleColorBar.setBackgroundColor(binding.root.context.getColor(colorRes))

            if (onEdit == null) {
                binding.editScheduleButton.visibility = android.view.View.GONE
            } else {
                binding.editScheduleButton.visibility = android.view.View.VISIBLE
                binding.editScheduleButton.setOnClickListener { onEdit.invoke(item) }
            }

            if (onDelete == null) {
                binding.deleteScheduleButton.visibility = android.view.View.GONE
            } else {
                binding.deleteScheduleButton.visibility = android.view.View.VISIBLE
                binding.deleteScheduleButton.setOnClickListener { onDelete.invoke(item) }
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean = oldItem == newItem
    }
}

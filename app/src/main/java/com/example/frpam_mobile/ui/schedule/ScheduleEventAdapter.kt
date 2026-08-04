package com.example.frpam_mobile.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frpam_mobile.data.model.ScheduleEvent
import com.example.frpam_mobile.databinding.ItemScheduleEventBinding
import java.time.format.DateTimeFormatter

class ScheduleEventAdapter(
    private val onEventClick: (ScheduleEvent) -> Unit
) : ListAdapter<ScheduleEvent, ScheduleEventAdapter.ViewHolder>(Diff) {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    object Diff : DiffUtil.ItemCallback<ScheduleEvent>() {
        override fun areItemsTheSame(oldItem: ScheduleEvent, newItem: ScheduleEvent) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ScheduleEvent, newItem: ScheduleEvent) =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemScheduleEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ScheduleEvent) {
            binding.tvTitle.text = event.title
            binding.tvTime.text = binding.root.context.getString(
                com.example.frpam_mobile.R.string.schedule_time_range,
                event.startDateTime.format(timeFormatter),
                event.endDateTime.format(timeFormatter)
            )
            binding.tvExperiment.text = event.experimentName
            binding.viewColorBar.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    ScheduleColorHelper.eventColor(event.colorIndex)
                )
            )
            binding.root.setOnClickListener { onEventClick(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScheduleEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

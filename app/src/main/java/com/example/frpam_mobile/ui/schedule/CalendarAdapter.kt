package com.example.frpam_mobile.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frpam_mobile.data.model.CalendarDay
import com.example.frpam_mobile.databinding.ItemCalendarDayBinding
import com.example.frpam_mobile.databinding.ItemCalendarEventChipBinding

class CalendarAdapter(
    private val onDayClick: (CalendarDay) -> Unit
) : ListAdapter<CalendarDay, CalendarAdapter.DayViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<CalendarDay>() {
        override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay) =
            oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay) =
            oldItem == newItem
    }

    inner class DayViewHolder(private val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: CalendarDay) {
            binding.tvDayNumber.text = day.date.dayOfMonth.toString()

            if (day.isToday) {
                binding.tvDayNumber.setBackgroundResource(com.example.frpam_mobile.R.drawable.bg_calendar_today)
                binding.tvDayNumber.setTextColor(
                    ContextCompat.getColor(binding.root.context, com.example.frpam_mobile.R.color.white)
                )
            } else {
                binding.tvDayNumber.background = null
                val textColor = if (day.isCurrentMonth) {
                    com.example.frpam_mobile.R.color.text_primary
                } else {
                    com.example.frpam_mobile.R.color.cal_day_outside
                }
                binding.tvDayNumber.setTextColor(
                    ContextCompat.getColor(binding.root.context, textColor)
                )
            }

            binding.layoutEvents.removeAllViews()
            val maxChips = 2
            day.events.take(maxChips).forEach { event ->
                val chipBinding = ItemCalendarEventChipBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    binding.layoutEvents,
                    false
                )
                chipBinding.tvEventChip.text = event.title
                chipBinding.tvEventChip.setBackgroundResource(
                    ScheduleColorHelper.chipBackground(event.colorIndex)
                )
                binding.layoutEvents.addView(chipBinding.root)
            }

            val remaining = day.events.size - maxChips
            if (remaining > 0) {
                binding.tvMoreEvents.visibility = View.VISIBLE
                binding.tvMoreEvents.text =
                    binding.root.context.getString(
                        com.example.frpam_mobile.R.string.schedule_more_events,
                        remaining
                    )
            } else {
                binding.tvMoreEvents.visibility = View.GONE
            }

            binding.dayCellRoot.setOnClickListener { onDayClick(day) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemCalendarDayBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

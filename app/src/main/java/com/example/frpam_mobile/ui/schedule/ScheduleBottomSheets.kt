package com.example.frpam_mobile.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frpam_mobile.data.model.ScheduleEvent
import com.example.frpam_mobile.databinding.BottomSheetDayEventsBinding
import com.example.frpam_mobile.databinding.BottomSheetEventDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DayEventsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDayEventsBinding? = null
    private val binding get() = _binding!!

    private var date: LocalDate = LocalDate.now()
    private var events: List<ScheduleEvent> = emptyList()
    private var onEventClick: ((ScheduleEvent) -> Unit)? = null

    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDayEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSheetDate.text = date.format(dateFormatter)

        if (events.isEmpty()) {
            binding.rvDayEvents.visibility = View.GONE
            binding.tvEmptyDay.visibility = View.VISIBLE
        } else {
            binding.rvDayEvents.visibility = View.VISIBLE
            binding.tvEmptyDay.visibility = View.GONE
            val adapter = ScheduleEventAdapter { event ->
                onEventClick?.invoke(event)
                dismiss()
            }
            binding.rvDayEvents.layoutManager = LinearLayoutManager(requireContext())
            binding.rvDayEvents.adapter = adapter
            adapter.submitList(events)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            date: LocalDate,
            events: List<ScheduleEvent>,
            onEventClick: (ScheduleEvent) -> Unit
        ): DayEventsBottomSheet {
            return DayEventsBottomSheet().apply {
                this.date = date
                this.events = events
                this.onEventClick = onEventClick
            }
        }
    }
}

class EventDetailBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEventDetailBinding? = null
    private val binding get() = _binding!!

    private var event: ScheduleEvent? = null

    private val dateTimeFormatter =
        DateTimeFormatter.ofPattern("EEEE, MMM d • HH:mm", Locale.ENGLISH)
    private val endTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = event ?: return
        binding.tvDetailTitle.text = item.title
        binding.tvDetailDateTime.text = buildString {
            append(item.startDateTime.format(dateTimeFormatter))
            append(" – ")
            append(item.endDateTime.format(endTimeFormatter))
        }
        binding.tvDetailExperiment.text = item.experimentName
        binding.tvDetailStatus.text = item.status
        binding.tvDetailDescription.text = item.description.orEmpty()

        val colorRes = ScheduleColorHelper.eventColor(item.colorIndex)
        binding.viewEventColor.setBackgroundColor(
            ContextCompat.getColor(requireContext(), colorRes)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(event: ScheduleEvent): EventDetailBottomSheet {
            return EventDetailBottomSheet().apply {
                this.event = event
            }
        }
    }
}

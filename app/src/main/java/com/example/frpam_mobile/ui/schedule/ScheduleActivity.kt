package com.example.frpam_mobile.ui.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frpam_mobile.data.mock.ScheduleMockData
import com.example.frpam_mobile.databinding.ActivityScheduleBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var calendarAdapter: CalendarAdapter

    private var currentMonth: YearMonth = YearMonth.now()
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarAdapter = CalendarAdapter(::onDayClick)
        binding.rvCalendar.apply {
            layoutManager = GridLayoutManager(this@ScheduleActivity, 7)
            adapter = calendarAdapter
            setHasFixedSize(true)
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.btnPrevMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            renderMonth()
        }
        binding.btnNextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            renderMonth()
        }
        binding.btnToday.setOnClickListener {
            currentMonth = YearMonth.now()
            renderMonth()
        }

        renderMonth()
    }

    private fun renderMonth() {
        binding.tvMonthYear.text = currentMonth.format(monthFormatter)
        val events = ScheduleMockData.eventsForMonth(currentMonth)
        val days = CalendarMonthHelper.buildMonthDays(currentMonth, events)
        calendarAdapter.submitList(days)
    }

    private fun onDayClick(day: com.example.frpam_mobile.data.model.CalendarDay) {
        showDayEvents(day.date, day.events)
    }

    private fun showDayEvents(date: LocalDate, events: List<com.example.frpam_mobile.data.model.ScheduleEvent>) {
        DayEventsBottomSheet.newInstance(date, events) { event ->
            EventDetailBottomSheet.newInstance(event)
                .show(supportFragmentManager, "event_detail")
        }.show(supportFragmentManager, "day_events")
    }
}

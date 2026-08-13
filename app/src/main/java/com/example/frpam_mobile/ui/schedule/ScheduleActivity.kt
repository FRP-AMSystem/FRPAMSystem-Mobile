package com.example.frpam_mobile.ui.schedule

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frpam_mobile.R
import com.example.frpam_mobile.data.model.CalendarDay
import com.example.frpam_mobile.data.model.ScheduleEvent
import com.example.frpam_mobile.data.prefs.SessionManager
import com.example.frpam_mobile.data.repository.ScheduleRepository
import com.example.frpam_mobile.data.repository.ScheduleResult
import com.example.frpam_mobile.databinding.ActivityScheduleBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var sessionManager: SessionManager

    private val repository = ScheduleRepository()
    private var currentMonth: YearMonth = YearMonth.now()
    private var monthEvents: List<ScheduleEvent> = emptyList()

    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (!sessionManager.canAccessSchedule()) {
            Toast.makeText(this, R.string.schedule_not_allowed, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        calendarAdapter = CalendarAdapter(::onDayClick)
        binding.rvCalendar.apply {
            layoutManager = GridLayoutManager(this@ScheduleActivity, 7)
            adapter = calendarAdapter
            setHasFixedSize(true)
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.btnPrevMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            loadMonth()
        }
        binding.btnNextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            loadMonth()
        }
        binding.btnToday.setOnClickListener {
            currentMonth = YearMonth.now()
            loadMonth()
        }

        loadMonth()
    }

    private fun loadMonth() {
        binding.tvMonthYear.text = currentMonth.format(monthFormatter)

        lifecycleScope.launch {
            binding.progressBar.isVisible = true

            when (val result = repository.getMySchedulesForMonth(currentMonth)) {
                is ScheduleResult.Success -> {
                    monthEvents = result.items
                    renderMonth(result.items)
                }
                is ScheduleResult.Error -> {
                    monthEvents = emptyList()
                    renderMonth(emptyList())
                    Toast.makeText(this@ScheduleActivity, result.message, Toast.LENGTH_LONG).show()
                }
            }

            binding.progressBar.isVisible = false
        }
    }

    private fun renderMonth(events: List<ScheduleEvent>) {
        val days = CalendarMonthHelper.buildMonthDays(currentMonth, events)
        calendarAdapter.submitList(days)
    }

    private fun onDayClick(day: CalendarDay) {
        showDayEvents(day.date, day.events)
    }

    private fun showDayEvents(date: LocalDate, events: List<ScheduleEvent>) {
        DayEventsBottomSheet.newInstance(date, events) { event ->
            lifecycleScope.launch {
                val detail = repository.getMyScheduleDetail(event.id) ?: event
                EventDetailBottomSheet.newInstance(detail)
                    .show(supportFragmentManager, "event_detail")
            }
        }.show(supportFragmentManager, "day_events")
    }
}

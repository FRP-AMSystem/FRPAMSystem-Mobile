package com.example.frpam_mobile.ui.schedule

import com.example.frpam_mobile.data.model.CalendarDay
import com.example.frpam_mobile.data.model.ScheduleEvent
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

object CalendarMonthHelper {

    /** Tuần bắt đầu từ Thứ 2 (giống Google Calendar VN). */
    fun buildMonthDays(
        yearMonth: YearMonth,
        events: List<ScheduleEvent>
    ): List<CalendarDay> {
        val today = LocalDate.now()
        val firstOfMonth = yearMonth.atDay(1)
        val gridStart = firstOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val lastOfMonth = yearMonth.atEndOfMonth()
        val gridEnd = lastOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val eventsByDate = events.groupBy { it.startDateTime.toLocalDate() }

        val days = mutableListOf<CalendarDay>()
        var cursor = gridStart
        while (!cursor.isAfter(gridEnd)) {
            val dayEvents = eventsForDate(cursor, events, eventsByDate)
            days.add(
                CalendarDay(
                    date = cursor,
                    isCurrentMonth = cursor.month == yearMonth.month,
                    isToday = cursor == today,
                    events = dayEvents
                )
            )
            cursor = cursor.plusDays(1)
        }
        return days
    }

    private fun eventsForDate(
        date: LocalDate,
        allEvents: List<ScheduleEvent>,
        eventsByDate: Map<LocalDate, List<ScheduleEvent>>
    ): List<ScheduleEvent> {
        val direct = eventsByDate[date].orEmpty()
        val spanning = allEvents.filter { event ->
            val start = event.startDateTime.toLocalDate()
            val end = event.endDateTime.toLocalDate()
            !date.isBefore(start) && !date.isAfter(end)
        }
        return (direct + spanning).distinctBy { it.id }.sortedBy { it.startDateTime }
    }
}

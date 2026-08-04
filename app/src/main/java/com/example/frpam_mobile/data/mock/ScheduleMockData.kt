package com.example.frpam_mobile.data.mock

import com.example.frpam_mobile.data.model.ScheduleEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * Dữ liệu mẫu — thay bằng API Schedule (AssignedHumanResourceId) sau.
 */
object ScheduleMockData {

    private val allEvents: List<ScheduleEvent> = buildSampleEvents()

    fun eventsForMonth(yearMonth: YearMonth): List<ScheduleEvent> {
        val start = yearMonth.atDay(1).atStartOfDay()
        val end = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay()
        return allEvents.filter { event ->
            event.startDateTime.isBefore(end) && event.endDateTime.isAfter(start)
        }
    }

    fun eventsForDate(date: LocalDate): List<ScheduleEvent> {
        return allEvents.filter { event ->
            val eventDate = event.startDateTime.toLocalDate()
            val eventEndDate = event.endDateTime.toLocalDate()
            !date.isBefore(eventDate) && !date.isAfter(eventEndDate)
        }.sortedBy { it.startDateTime }
    }

    private fun buildSampleEvents(): List<ScheduleEvent> {
        val today = LocalDate.now()
        val month = YearMonth.from(today)

        return listOf(
            event(
                id = 1,
                experimentId = 101,
                name = "Soil Moisture Monitoring",
                title = "Field sampling — Phase 1",
                description = "Collect soil samples and record moisture levels at assigned plots.",
                date = month.atDay(5),
                startHour = 8, startMin = 30,
                endHour = 11, endMin = 0,
                status = "Scheduled",
                colorIndex = 0
            ),
            event(
                id = 2,
                experimentId = 102,
                name = "Seed Germination Study",
                title = "Germination observation",
                description = "Daily observation and data logging for germination rates.",
                date = month.atDay(5),
                startHour = 13, startMin = 0,
                endHour = 15, endMin = 30,
                status = "Scheduled",
                colorIndex = 1
            ),
            event(
                id = 3,
                experimentId = 103,
                name = "Canopy Cover Analysis",
                title = "Canopy measurement",
                description = "Use densitometer at grid points across the study area.",
                date = month.atDay(12),
                startHour = 9, startMin = 0,
                endHour = 12, endMin = 0,
                status = "In Progress",
                colorIndex = 2
            ),
            event(
                id = 4,
                experimentId = 101,
                name = "Soil Moisture Monitoring",
                title = "Lab analysis session",
                description = "Process collected samples in the lab facility.",
                date = month.atDay(18),
                startHour = 14, startMin = 0,
                endHour = 17, endMin = 0,
                status = "Scheduled",
                colorIndex = 0
            ),
            event(
                id = 5,
                experimentId = 104,
                name = "Tree Growth Tracking",
                title = "Monthly height measurement",
                description = "Measure and record tree height for all tagged specimens.",
                date = month.atDay(22),
                startHour = 7, startMin = 30,
                endHour = 10, endMin = 30,
                status = "Scheduled",
                colorIndex = 3
            ),
            event(
                id = 6,
                experimentId = 105,
                name = "Water Runoff Experiment",
                title = "Runoff data collection",
                description = "Monitor runoff channels after controlled irrigation.",
                date = month.atDay(22),
                startHour = 12, startMin = 45,
                endHour = 15, endMin = 45,
                status = "Scheduled",
                colorIndex = 4
            ),
            event(
                id = 7,
                experimentId = 102,
                name = "Seed Germination Study",
                title = "Final phase review",
                description = "Summarize germination data and prepare field notes.",
                date = month.atDay(28),
                startHour = 10, startMin = 0,
                endHour = 11, endMin = 30,
                status = "Scheduled",
                colorIndex = 1
            )
        )
    }

    private fun event(
        id: Int,
        experimentId: Int,
        name: String,
        title: String,
        description: String,
        date: LocalDate,
        startHour: Int,
        startMin: Int,
        endHour: Int,
        endMin: Int,
        status: String,
        colorIndex: Int
    ): ScheduleEvent {
        return ScheduleEvent(
            id = id,
            experimentId = experimentId,
            experimentName = name,
            title = title,
            description = description,
            startDateTime = LocalDateTime.of(date, java.time.LocalTime.of(startHour, startMin)),
            endDateTime = LocalDateTime.of(date, java.time.LocalTime.of(endHour, endMin)),
            status = status,
            colorIndex = colorIndex
        )
    }
}

package com.example.frpam_mobile.data.model

import java.time.LocalDate
import java.time.LocalDateTime

/** Lịch thí nghiệm được assign cho user (researcher / student). */
data class ScheduleEvent(
    val id: Int,
    val experimentId: Int,
    val experimentName: String,
    val title: String,
    val description: String?,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: String,
    val colorIndex: Int
)

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val events: List<ScheduleEvent>
)

package com.example.frpam_mobile.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
    val colorIndex: Int,
    val phaseName: String? = null,
    val notes: String? = null
)

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val events: List<ScheduleEvent>
)

data class ScheduleItem(
    @SerializedName("scheduleId") val scheduleId: Int = 0,
    @SerializedName("allocationPlanId") val allocationPlanId: Int = 0,
    @SerializedName("experimentId") val experimentId: Int = 0,
    @SerializedName("experimentName") val experimentName: String? = null,
    @SerializedName("phaseId") val phaseId: Int? = null,
    @SerializedName("phaseName") val phaseName: String? = null,
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String? = null,
    @SerializedName("startDate") val startDate: String = "",
    @SerializedName("endDate") val endDate: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("assignedHumanResourceId") val assignedHumanResourceId: Int? = null,
    @SerializedName("assignedHumanResourceName") val assignedHumanResourceName: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("priority") val priority: Int = 0
) {
    fun toScheduleEvent(): ScheduleEvent {
        return ScheduleEvent(
            id = scheduleId,
            experimentId = experimentId,
            experimentName = experimentName.orEmpty(),
            title = title,
            description = description,
            startDateTime = parseDateTime(startDate),
            endDateTime = parseEndDateTime(endDate),
            status = status,
            colorIndex = experimentId % 5,
            phaseName = phaseName,
            notes = notes
        )
    }

    private fun parseDateTime(value: String): LocalDateTime {
        return runCatching { LocalDateTime.parse(value) }
            .getOrElse {
                runCatching { LocalDate.parse(value.substringBefore('T')).atStartOfDay() }
                    .getOrDefault(LocalDateTime.now())
            }
    }

    private fun parseEndDateTime(value: String): LocalDateTime {
        return runCatching { LocalDateTime.parse(value) }
            .getOrElse {
                runCatching {
                    val date = LocalDate.parse(value.substringBefore('T'))
                    if (value.length <= 10) date.atTime(LocalTime.of(23, 59)) else date.atStartOfDay()
                }.getOrDefault(LocalDateTime.now())
            }
    }
}

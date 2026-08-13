package com.example.frpam_mobile.data.repository

import com.example.frpam_mobile.data.api.RetrofitClient
import com.example.frpam_mobile.data.model.ScheduleEvent
import com.example.frpam_mobile.data.model.ScheduleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.YearMonth
import java.time.format.DateTimeFormatter

sealed class ScheduleResult {
    data class Success(val items: List<ScheduleEvent>) : ScheduleResult()
    data class Error(val message: String) : ScheduleResult()
}

class ScheduleRepository {

    suspend fun getMySchedulesForMonth(yearMonth: YearMonth): ScheduleResult =
        withContext(Dispatchers.IO) {
            try {
                val dateFrom = yearMonth.atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
                val dateTo = yearMonth.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE)

                val response = RetrofitClient.scheduleApi.getMySchedules(
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    page = 1,
                    size = 100
                )
                val body = response.body()

                when {
                    response.isSuccessful && body?.success == true -> {
                        ScheduleResult.Success(
                            body.data?.items.orEmpty().map(ScheduleItem::toScheduleEvent)
                        )
                    }
                    response.code() == 403 -> {
                        ScheduleResult.Error("Schedule is only available for Researcher and Student roles.")
                    }
                    else -> {
                        ScheduleResult.Error(body?.message ?: "Failed to load schedules.")
                    }
                }
            } catch (e: Exception) {
                ScheduleResult.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Cannot connect to server."
                )
            }
        }

    suspend fun getMyScheduleDetail(scheduleId: Int): ScheduleEvent? = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.scheduleApi.getMyScheduleById(scheduleId)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.toScheduleEvent()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}

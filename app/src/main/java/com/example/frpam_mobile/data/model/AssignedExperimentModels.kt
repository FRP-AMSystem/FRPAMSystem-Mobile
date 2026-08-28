package com.example.frpam_mobile.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class AllocationHumanItem(
    @SerializedName("allocationHumanDetailId") val allocationHumanDetailId: Int = 0,
    @SerializedName("allocationPlanId") val allocationPlanId: Int = 0,
    @SerializedName("experimentId") val experimentId: Int = 0,
    @SerializedName("experimentName") val experimentName: String? = null,
    @SerializedName("phaseId") val phaseId: Int? = null,
    @SerializedName("phaseName") val phaseName: String? = null,
    @SerializedName("requiredRoleName") val requiredRoleName: String? = null,
    @SerializedName("requiredSkillName") val requiredSkillName: String? = null,
    @SerializedName("workingHours") val workingHours: Double = 0.0,
    @SerializedName("maxWorkingHoursPerDay") val maxWorkingHoursPerDay: Double = 0.0,
    @SerializedName("currentWorkload") val currentWorkload: Double = 0.0,
    @SerializedName("requirementNote") val requirementNote: String? = null,
    @SerializedName("startDate") val startDate: String = "",
    @SerializedName("endDate") val endDate: String = "",
    @SerializedName("status") val status: String = ""
) {
    fun displayTitle(): String = experimentName?.takeIf { it.isNotBlank() } ?: "Experiment #$experimentId"

    fun displaySubtitle(): String {
        val parts = listOfNotNull(
            phaseName?.takeIf { it.isNotBlank() }?.let { "Phase: $it" },
            requiredRoleName?.takeIf { it.isNotBlank() }
        )
        return parts.joinToString(" • ").ifBlank { "Assigned assignment" }
    }

    fun displayDateRange(): String {
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
        val start = parseDate(startDate)?.format(formatter).orEmpty()
        val end = parseDate(endDate)?.format(formatter).orEmpty()
        return when {
            start.isNotBlank() && end.isNotBlank() -> "$start – $end"
            start.isNotBlank() -> start
            end.isNotBlank() -> end
            else -> ""
        }
    }

    fun colorIndex(): Int = experimentId % 5

    private fun parseDate(value: String): LocalDate? =
        runCatching { LocalDate.parse(value.substringBefore('T')) }.getOrNull()
}

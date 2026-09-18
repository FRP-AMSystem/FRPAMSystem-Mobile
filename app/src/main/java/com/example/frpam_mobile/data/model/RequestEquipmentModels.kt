package com.example.frpam_mobile.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class AllocationEquipmentItem(
    @SerializedName("allocationEquipmentDetailId") val allocationEquipmentDetailId: Int = 0,
    @SerializedName("allocationPlanId") val allocationPlanId: Int = 0,
    @SerializedName("experimentId") val experimentId: Int = 0,
    @SerializedName("experimentName") val experimentName: String? = null,
    @SerializedName("phaseName") val phaseName: String? = null,
    @SerializedName("requestedEquipmentTypeName") val requestedEquipmentTypeName: String? = null,
    @SerializedName("allocatedEquipmentTypeName") val allocatedEquipmentTypeName: String? = null,
    @SerializedName("trackingType") val trackingType: String? = null,
    @SerializedName("equipmentInstanceId") val equipmentInstanceId: Int? = null,
    @SerializedName("assetCode") val assetCode: String? = null,
    @SerializedName("serialNumber") val serialNumber: String? = null,
    @SerializedName("quantity") val quantity: Int = 0,
    @SerializedName("isSubstitute") val isSubstitute: Boolean = false,
    @SerializedName("startDate") val startDate: String = "",
    @SerializedName("endDate") val endDate: String = "",
    @SerializedName("status") val status: String = ""
) {
    fun displayTitle(): String =
        allocatedEquipmentTypeName?.takeIf { it.isNotBlank() }
            ?: requestedEquipmentTypeName?.takeIf { it.isNotBlank() }
            ?: "Equipment #$allocationEquipmentDetailId"

    fun displaySubtitle(): String {
        val parts = listOfNotNull(
            experimentName?.takeIf { it.isNotBlank() },
            phaseName?.takeIf { it.isNotBlank() }?.let { "Phase: $it" },
            assetCode?.takeIf { it.isNotBlank() }?.let { "Asset: $it" }
        )
        return parts.joinToString(" • ").ifBlank { "Allocated equipment" }
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

    fun canHandover(): Boolean {
        val value = status.trim()
        return value.equals("Reserved", ignoreCase = true) ||
            value.equals("Allocated", ignoreCase = true)
    }

    fun canReturn(): Boolean {
        val value = status.trim()
        return value.equals("InUse", ignoreCase = true) ||
            value.equals("In Use", ignoreCase = true) ||
            value.equals("Active", ignoreCase = true)
    }

    private fun parseDate(value: String): LocalDate? =
        runCatching { LocalDate.parse(value.substringBefore('T')) }.getOrNull()
}

data class HandoverMineRequest(
    @SerializedName("conditionBefore") val conditionBefore: String? = null,
    @SerializedName("note") val note: String? = null
)

data class ReturnMineRequest(
    @SerializedName("conditionAfter") val conditionAfter: String,
    @SerializedName("isDamaged") val isDamaged: Boolean = false,
    @SerializedName("damageDescription") val damageDescription: String? = null,
    @SerializedName("note") val note: String? = null
)

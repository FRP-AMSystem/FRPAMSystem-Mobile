package com.example.frpam_mobile.data.repository

import com.example.frpam_mobile.data.api.RetrofitClient
import com.example.frpam_mobile.data.model.AllocationEquipmentItem
import com.example.frpam_mobile.data.model.HandoverMineRequest
import com.example.frpam_mobile.data.model.ReturnMineRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

sealed class RequestEquipmentResult {
    data class Success(val items: List<AllocationEquipmentItem>) : RequestEquipmentResult()
    data class Error(val message: String) : RequestEquipmentResult()
}

sealed class RequestEquipmentActionResult {
    data object Success : RequestEquipmentActionResult()
    data class Error(val message: String) : RequestEquipmentActionResult()
}

class RequestEquipmentRepository {

    private val gson = Gson()

    suspend fun getMyEquipment(page: Int = 1, size: Int = 50): RequestEquipmentResult =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.allocationEquipmentApi.getMyEquipment(page = page, size = size)
                val body = response.body()
                when {
                    response.isSuccessful && body?.success == true -> {
                        RequestEquipmentResult.Success(body.data?.items.orEmpty())
                    }
                    response.code() == 403 -> {
                        RequestEquipmentResult.Error(
                            "You do not have permission to view allocated equipment."
                        )
                    }
                    else -> {
                        RequestEquipmentResult.Error(
                            body?.message ?: parseErrorMessage(response, "Failed to load allocated equipment.")
                        )
                    }
                }
            } catch (e: Exception) {
                RequestEquipmentResult.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Cannot connect to server."
                )
            }
        }

    suspend fun getEquipmentDetail(id: Int): AllocationEquipmentItem? = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.allocationEquipmentApi.getMyEquipmentById(id)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun handover(id: Int, note: String? = null): RequestEquipmentActionResult =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.equipmentHandoverApi.submitMineHandover(
                    allocationEquipmentDetailId = id,
                    request = HandoverMineRequest(note = note)
                )
                mapAction(response, "Equipment handover confirmed.")
            } catch (e: Exception) {
                RequestEquipmentActionResult.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Cannot connect to server."
                )
            }
        }

    suspend fun returnEquipment(id: Int, request: ReturnMineRequest): RequestEquipmentActionResult =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.equipmentReturnApi.submitMineReturn(id, request)
                mapAction(response, "Equipment returned.")
            } catch (e: Exception) {
                RequestEquipmentActionResult.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Cannot connect to server."
                )
            }
        }

    private fun mapAction(
        response: Response<*>,
        fallback: String
    ): RequestEquipmentActionResult {
        if (response.isSuccessful) {
            return RequestEquipmentActionResult.Success
        }
        if (response.code() == 403) {
            return RequestEquipmentActionResult.Error(
                "You do not have permission to perform this action."
            )
        }
        return RequestEquipmentActionResult.Error(parseErrorMessage(response, fallback))
    }

    private fun parseErrorMessage(response: Response<*>, fallback: String): String {
        val raw = response.errorBody()?.string()
        if (raw.isNullOrBlank()) return fallback
        return try {
            val json = gson.fromJson(raw, JsonObject::class.java)
            when {
                json.has("message") -> json.get("message").asString
                json.has("title") -> json.get("title").asString
                else -> fallback
            }
        } catch (_: Exception) {
            fallback
        }
    }
}

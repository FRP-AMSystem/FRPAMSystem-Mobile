package com.example.frpam_mobile.data.repository

import com.example.frpam_mobile.data.api.RetrofitClient
import com.example.frpam_mobile.data.model.AllocationHumanItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class AssignedExperimentsResult {
    data class Success(val items: List<AllocationHumanItem>) : AssignedExperimentsResult()
    data class Error(val message: String) : AssignedExperimentsResult()
}

class AssignedExperimentRepository {

    suspend fun getMyAssignments(page: Int = 1, size: Int = 50): AssignedExperimentsResult =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.allocationHumanApi.getMyAssignments(page = page, size = size)
                val body = response.body()

                when {
                    response.isSuccessful && body?.success == true -> {
                        AssignedExperimentsResult.Success(body.data?.items.orEmpty())
                    }
                    response.code() == 403 -> {
                        AssignedExperimentsResult.Error(
                            "You do not have permission to view assigned experiments."
                        )
                    }
                    else -> {
                        AssignedExperimentsResult.Error(
                            body?.message ?: "Failed to load assigned experiments."
                        )
                    }
                }
            } catch (e: Exception) {
                AssignedExperimentsResult.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Cannot connect to server."
                )
            }
        }

    suspend fun getAssignmentDetail(id: Int): AllocationHumanItem? = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.allocationHumanApi.getMyAssignmentById(id)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}

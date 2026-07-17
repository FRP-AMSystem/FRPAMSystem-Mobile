package com.example.frpam_mobile.data.repository

import com.example.frpam_mobile.data.api.RetrofitClient
import com.example.frpam_mobile.data.model.NotificationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class NotificationsResult {
    data class Success(val items: List<NotificationItem>) : NotificationsResult()
    data class Error(val message: String) : NotificationsResult()
}

class NotificationRepository {

    suspend fun getMyNotifications(page: Int = 1, size: Int = 50): NotificationsResult =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.notificationApi.getMyNotifications(page, size)
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    NotificationsResult.Success(body.data?.items ?: emptyList())
                } else {
                    NotificationsResult.Error(
                        body?.message ?: "Failed to load notifications."
                    )
                }
            } catch (e: Exception) {
                NotificationsResult.Error(
                    e.message?.takeIf { it.isNotBlank() }
                        ?: "Cannot connect to server."
                )
            }
        }

    suspend fun getUnreadCount(): Int = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.notificationApi.getUnreadCount()
            if (response.isSuccessful) {
                response.body()?.data?.unreadCount ?: 0
            } else {
                0
            }
        } catch (_: Exception) {
            0
        }
    }

    suspend fun markAsRead(notificationId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.notificationApi.markAsRead(notificationId)
            response.isSuccessful && response.body()?.success == true
        } catch (_: Exception) {
            false
        }
    }
}

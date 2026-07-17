package com.example.frpam_mobile.data.model

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("size") val size: Int = 0,
    @SerializedName("total") val total: Int = 0,
    @SerializedName("totalPages") val totalPages: Int = 0,
    @SerializedName("items") val items: List<T> = emptyList()
)

data class NotificationItem(
    @SerializedName("notificationId") val notificationId: Int = 0,
    @SerializedName("userId") val userId: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("notificationType") val notificationType: String = "",
    @SerializedName("referenceType") val referenceType: String? = null,
    @SerializedName("referenceId") val referenceId: Int? = null,
    @SerializedName("isRead") val isRead: Boolean = false,
    @SerializedName("readAt") val readAt: String? = null,
    @SerializedName("createdAt") val createdAt: String = ""
)

data class UnreadCountData(
    @SerializedName("unreadCount") val unreadCount: Int = 0
)

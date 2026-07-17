package com.example.frpam_mobile.data.api

import com.example.frpam_mobile.data.model.ApiResponse
import com.example.frpam_mobile.data.model.NotificationItem
import com.example.frpam_mobile.data.model.PageResponse
import com.example.frpam_mobile.data.model.UnreadCountData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @GET("api/Notifications")
    suspend fun getMyNotifications(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<PageResponse<NotificationItem>>>

    @GET("api/Notifications/unread-count")
    suspend fun getUnreadCount(): Response<ApiResponse<UnreadCountData>>

    @PATCH("api/Notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: Int): Response<ApiResponse<Any>>

    @PATCH("api/Notifications/read-all")
    suspend fun markAllAsRead(): Response<ApiResponse<Any>>
}

package com.example.frpam_mobile.data.api

import com.example.frpam_mobile.data.model.ApiResponse
import com.example.frpam_mobile.data.model.PageResponse
import com.example.frpam_mobile.data.model.ScheduleItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {

    @GET("api/Schedules/mine")
    suspend fun getMySchedules(
        @Query("DateFrom") dateFrom: String? = null,
        @Query("DateTo") dateTo: String? = null,
        @Query("Status") status: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 100
    ): Response<ApiResponse<PageResponse<ScheduleItem>>>

    @GET("api/Schedules/mine/{id}")
    suspend fun getMyScheduleById(
        @Path("id") id: Int
    ): Response<ApiResponse<ScheduleItem>>
}

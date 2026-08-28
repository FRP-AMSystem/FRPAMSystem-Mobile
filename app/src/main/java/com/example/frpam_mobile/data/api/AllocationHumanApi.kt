package com.example.frpam_mobile.data.api

import com.example.frpam_mobile.data.model.AllocationHumanItem
import com.example.frpam_mobile.data.model.ApiResponse
import com.example.frpam_mobile.data.model.PageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AllocationHumanApi {

    @GET("api/AllocationHumanDetails/mine")
    suspend fun getMyAssignments(
        @Query("Keyword") keyword: String? = null,
        @Query("Status") status: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 50
    ): Response<ApiResponse<PageResponse<AllocationHumanItem>>>

    @GET("api/AllocationHumanDetails/mine/{id}")
    suspend fun getMyAssignmentById(
        @Path("id") id: Int
    ): Response<ApiResponse<AllocationHumanItem>>
}

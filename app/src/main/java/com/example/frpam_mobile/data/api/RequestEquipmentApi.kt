package com.example.frpam_mobile.data.api

import com.example.frpam_mobile.data.model.AllocationEquipmentItem
import com.example.frpam_mobile.data.model.ApiResponse
import com.example.frpam_mobile.data.model.HandoverMineRequest
import com.example.frpam_mobile.data.model.PageResponse
import com.example.frpam_mobile.data.model.ReturnMineRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface AllocationEquipmentApi {

    @GET("api/AllocationEquipmentDetails/mine")
    suspend fun getMyEquipment(
        @Query("Keyword") keyword: String? = null,
        @Query("Status") status: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 50
    ): Response<ApiResponse<PageResponse<AllocationEquipmentItem>>>

    @GET("api/AllocationEquipmentDetails/mine/{id}")
    suspend fun getMyEquipmentById(
        @Path("id") id: Int
    ): Response<ApiResponse<AllocationEquipmentItem>>
}

interface EquipmentHandoverApi {

    @PATCH("api/EquipmentHandovers/mine/{allocationEquipmentDetailId}/handover")
    suspend fun submitMineHandover(
        @Path("allocationEquipmentDetailId") allocationEquipmentDetailId: Int,
        @Body request: HandoverMineRequest = HandoverMineRequest()
    ): Response<ApiResponse<Any>>
}

interface EquipmentReturnApi {

    @PATCH("api/EquipmentReturns/mine/{allocationEquipmentDetailId}/return")
    suspend fun submitMineReturn(
        @Path("allocationEquipmentDetailId") allocationEquipmentDetailId: Int,
        @Body request: ReturnMineRequest
    ): Response<ApiResponse<Any>>
}

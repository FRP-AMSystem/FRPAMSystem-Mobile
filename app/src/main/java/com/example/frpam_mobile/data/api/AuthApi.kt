package com.example.frpam_mobile.data.api

import com.example.frpam_mobile.data.model.ApiResponse
import com.example.frpam_mobile.data.model.LoginRequest
import com.example.frpam_mobile.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>
}

package com.example.frpam_mobile.data.api

import com.example.frpam_mobile.data.model.ApiResponse
import com.example.frpam_mobile.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("api/Users/me")
    suspend fun getCurrentUser(): Response<ApiResponse<UserProfile>>
}

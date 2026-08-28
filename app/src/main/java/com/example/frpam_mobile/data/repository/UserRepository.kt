package com.example.frpam_mobile.data.repository

import com.example.frpam_mobile.data.api.RetrofitClient
import com.example.frpam_mobile.data.model.UserProfile

sealed class UserProfileResult {
    data class Success(val profile: UserProfile) : UserProfileResult()
    data class Error(val message: String) : UserProfileResult()
}

class UserRepository {

    suspend fun getCurrentUserProfile(): UserProfileResult {
        return try {
            val response = RetrofitClient.userApi.getCurrentUser()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    UserProfileResult.Success(body.data)
                } else {
                    UserProfileResult.Error(body?.message ?: "Failed to load profile.")
                }
            } else {
                UserProfileResult.Error("Failed to load profile (${response.code()}).")
            }
        } catch (e: Exception) {
            UserProfileResult.Error(e.message ?: "Network error.")
        }
    }
}

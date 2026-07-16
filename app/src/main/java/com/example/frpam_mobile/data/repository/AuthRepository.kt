package com.example.frpam_mobile.data.repository

import com.example.frpam_mobile.data.api.RetrofitClient
import com.example.frpam_mobile.data.model.LoginRequest
import com.example.frpam_mobile.data.model.LoginResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class LoginResult {
    data class Success(val data: LoginResponse) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class AuthRepository {

    private val gson = Gson()

    suspend fun login(usernameOrEmail: String, password: String): LoginResult =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.authApi.login(
                    LoginRequest(
                        usernameOrEmail = usernameOrEmail.trim(),
                        password = password
                    )
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    val data = body?.data
                    if (body?.success == true && data != null && data.accessToken.isNotBlank()) {
                        LoginResult.Success(data)
                    } else {
                        LoginResult.Error(body?.message ?: "Login failed.")
                    }
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    LoginResult.Error(errorMessage)
                }
            } catch (e: Exception) {
                LoginResult.Error(
                    e.message?.takeIf { it.isNotBlank() }
                        ?: "Cannot connect to server. Please try again."
                )
            }
        }

    private fun parseErrorMessage(raw: String?): String {
        if (raw.isNullOrBlank()) return "Login failed. Please check your credentials."
        return try {
            val json = gson.fromJson(raw, JsonObject::class.java)
            when {
                json.has("message") -> json.get("message").asString
                json.has("title") -> json.get("title").asString
                else -> "Login failed. Please check your credentials."
            }
        } catch (_: Exception) {
            "Login failed. Please check your credentials."
        }
    }
}

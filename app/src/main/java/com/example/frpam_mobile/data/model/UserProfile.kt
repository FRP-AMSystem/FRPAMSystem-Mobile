package com.example.frpam_mobile.data.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("fullName") val fullName: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("roleName") val roleName: String = ""
)

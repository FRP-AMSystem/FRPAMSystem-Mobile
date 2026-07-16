package com.example.frpam_mobile.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.frpam_mobile.data.model.LoginResponse

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(login: LoginResponse, rememberMe: Boolean) {
        prefs.edit {
            putString(KEY_TOKEN, login.accessToken)
            putInt(KEY_USER_ID, login.userId)
            putString(KEY_FULL_NAME, login.fullName)
            putString(KEY_USERNAME, login.username)
            putString(KEY_EMAIL, login.email)
            putInt(KEY_ROLE_ID, login.roleId)
            putString(KEY_ROLE_NAME, login.roleName)
            putBoolean(KEY_REMEMBER_ME, rememberMe)
            putBoolean(KEY_IS_LOGGED_IN, true)
        }
    }

    fun clearSession() {
        val rememberedEmail = if (isRememberMe()) getEmail() else null
        prefs.edit {
            clear()
            if (rememberedEmail != null) {
                putString(KEY_SAVED_EMAIL, rememberedEmail)
                putBoolean(KEY_REMEMBER_ME, true)
            }
        }
    }

    fun saveRememberedEmail(email: String) {
        prefs.edit {
            putString(KEY_SAVED_EMAIL, email)
            putBoolean(KEY_REMEMBER_ME, true)
        }
    }

    fun clearRememberedEmail() {
        prefs.edit {
            remove(KEY_SAVED_EMAIL)
            putBoolean(KEY_REMEMBER_ME, false)
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun isRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getFullName(): String = prefs.getString(KEY_FULL_NAME, "") ?: ""

    fun getEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""

    fun getRoleName(): String = prefs.getString(KEY_ROLE_NAME, "") ?: ""

    fun getSavedEmail(): String = prefs.getString(KEY_SAVED_EMAIL, "") ?: ""

    companion object {
        private const val PREFS_NAME = "frpam_session"
        private const val KEY_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_ROLE_ID = "role_id"
        private const val KEY_ROLE_NAME = "role_name"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_SAVED_EMAIL = "saved_email"
    }
}

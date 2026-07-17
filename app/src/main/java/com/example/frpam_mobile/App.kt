package com.example.frpam_mobile

import android.app.Application
import com.example.frpam_mobile.data.api.RetrofitClient
import com.example.frpam_mobile.data.prefs.SessionManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sessionManager = SessionManager(this)
        RetrofitClient.setTokenProvider { sessionManager.getToken() }
    }
}

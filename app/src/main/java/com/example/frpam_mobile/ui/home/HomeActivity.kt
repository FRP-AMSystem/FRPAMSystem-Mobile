package com.example.frpam_mobile.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frpam_mobile.data.prefs.SessionManager
import com.example.frpam_mobile.databinding.ActivityHomeBinding
import com.example.frpam_mobile.ui.login.LoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            goToLogin()
            return
        }

        binding.tvUserName.text = sessionManager.getFullName()
        binding.tvUserRole.text = sessionManager.getRoleName()
        binding.tvUserEmail.text = sessionManager.getEmail()

        binding.btnLogout.setOnClickListener {
            sessionManager.clearSession()
            goToLogin()
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

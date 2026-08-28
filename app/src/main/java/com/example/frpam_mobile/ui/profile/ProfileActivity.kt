package com.example.frpam_mobile.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.frpam_mobile.data.prefs.SessionManager
import com.example.frpam_mobile.data.repository.UserProfileResult
import com.example.frpam_mobile.data.repository.UserRepository
import com.example.frpam_mobile.databinding.ActivityProfileBinding
import com.example.frpam_mobile.util.NameUtils
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sessionManager: SessionManager
    private val repository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnBack.setOnClickListener { finish() }

        showProfileFromSession()
        loadProfileFromApi()
    }

    private fun showProfileFromSession() {
        bindProfile(
            fullName = sessionManager.getFullName(),
            username = sessionManager.getUsername(),
            email = sessionManager.getEmail(),
            roleName = sessionManager.getRoleName()
        )
    }

    private fun loadProfileFromApi() {
        binding.progressBar.isVisible = true

        lifecycleScope.launch {
            when (val result = repository.getCurrentUserProfile()) {
                is UserProfileResult.Success -> {
                    bindProfile(
                        fullName = result.profile.fullName,
                        username = result.profile.username,
                        email = result.profile.email,
                        roleName = result.profile.roleName
                    )
                }
                is UserProfileResult.Error -> {
                    Toast.makeText(this@ProfileActivity, result.message, Toast.LENGTH_SHORT).show()
                }
            }
            binding.progressBar.isVisible = false
        }
    }

    private fun bindProfile(
        fullName: String,
        username: String,
        email: String,
        roleName: String
    ) {
        binding.tvAvatarInitial.text = NameUtils.getNameInitial(fullName)
        binding.tvFullName.text = fullName
        binding.tvUsername.text = username
        binding.tvEmail.text = email
        binding.tvRoleName.text = roleName
    }
}

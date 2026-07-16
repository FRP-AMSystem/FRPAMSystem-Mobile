package com.example.frpam_mobile.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.frpam_mobile.R
import com.example.frpam_mobile.data.prefs.SessionManager
import com.example.frpam_mobile.databinding.ActivityLoginBinding
import com.example.frpam_mobile.ui.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private val viewModel: LoginViewModel by viewModels()

    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            navigateToHome()
            return
        }

        restoreRememberedCredentials()
        setupListeners()
        observeViewModel()
    }

    private fun restoreRememberedCredentials() {
        if (sessionManager.isRememberMe()) {
            binding.cbRememberMe.isChecked = true
            val saved = sessionManager.getSavedEmail()
            if (saved.isNotBlank()) {
                binding.etEmail.setText(saved)
            }
        }
    }

    private fun setupListeners() {
        binding.tilPassword.setEndIconOnClickListener {
            togglePasswordVisibility()
        }

        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, R.string.forgot_password_coming_soon, Toast.LENGTH_SHORT).show()
        }

        binding.etPassword.setOnEditorActionListener { _, _, _ ->
            attemptLogin()
            true
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LoginUiState.Idle -> setLoading(false)
                    is LoginUiState.Loading -> setLoading(true)
                    is LoginUiState.Success -> {
                        setLoading(false)
                        val remember = binding.cbRememberMe.isChecked
                        sessionManager.saveSession(state.data, remember)
                        if (remember) {
                            sessionManager.saveRememberedEmail(
                                binding.etEmail.text?.toString()?.trim().orEmpty()
                            )
                        } else {
                            sessionManager.clearRememberedEmail()
                        }
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_success, state.data.fullName),
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHome()
                    }
                    is LoginUiState.Error -> {
                        setLoading(false)
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        if (email.isBlank() || password.isBlank()) {
            showError(getString(R.string.error_empty_credentials))
            return
        }

        hideError()
        viewModel.login(email, password)
    }

    private fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
        if (passwordVisible) {
            binding.etPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.tilPassword.setEndIconDrawable(R.drawable.ic_visibility)
        } else {
            binding.etPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.tilPassword.setEndIconDrawable(R.drawable.ic_visibility_off)
        }
        binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.btnLogin.isEnabled = !loading
        binding.etEmail.isEnabled = !loading
        binding.etPassword.isEnabled = !loading
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.tvError.visibility = View.GONE
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}

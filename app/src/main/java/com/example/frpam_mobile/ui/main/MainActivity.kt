package com.example.frpam_mobile.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.frpam_mobile.R
import com.example.frpam_mobile.data.prefs.SessionManager
import com.example.frpam_mobile.databinding.ActivityMainBinding
import com.example.frpam_mobile.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    private val homeFragment = HomeFragment()
    private val inboxFragment = InboxFragment()
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            goToLogin()
            return
        }

        setupBottomNav()
        setupAvatar()

        if (savedInstanceState == null) {
            showFragment(homeFragment, getString(R.string.home_title))
            binding.bottomNav.selectedItemId = R.id.nav_home
        }
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showFragment(homeFragment, getString(R.string.home_title))
                    true
                }
                R.id.nav_inbox -> {
                    showFragment(inboxFragment, getString(R.string.inbox_title))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAvatar() {
        binding.ivAvatar.setOnClickListener { view ->
            PopupMenu(this, view).apply {
                menu.add(sessionManager.getFullName()).isEnabled = false
                menu.add(getString(R.string.logout)).setOnMenuItemClickListener {
                    sessionManager.clearSession()
                    goToLogin()
                    true
                }
                show()
            }
        }
    }

    private fun showFragment(fragment: Fragment, title: String) {
        binding.tvPageTitle.text = title

        if (fragment == activeFragment) return

        val transaction = supportFragmentManager.beginTransaction()
        activeFragment?.let { transaction.hide(it) }
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.fragmentContainer, fragment)
        }
        transaction.commit()
        activeFragment = fragment
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

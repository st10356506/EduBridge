package com.example.edubridge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getString("user_email", null) != null
        username = sharedPref.getString("user_username", "User") // retrieve stored username

        if (isLoggedIn) {
            setContentView(R.layout.activity_main)

            bottomNavView = findViewById(R.id.bottom_navigation)

            if (savedInstanceState == null) {
                // Pass username to HomeFragment
                val homeFragment = HomeFragment()
                val bundle = Bundle()
                bundle.putString("username", username)
                homeFragment.arguments = bundle

                replaceFragment(homeFragment)
                bottomNavView.selectedItemId = R.id.nav_dashboard
            }

            bottomNavView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_dashboard -> {
                        val homeFragment = HomeFragment()
                        val bundle = Bundle()
                        bundle.putString("username", username)
                        homeFragment.arguments = bundle
                        replaceFragment(homeFragment)
                    }
                    R.id.nav_study -> replaceFragment(StudyFragment())
                    R.id.nav_chat -> replaceFragment(AiFragment())
                    R.id.nav_scan -> replaceFragment(ScanFragment())
                    R.id.nav_rewards -> replaceFragment(RewardsFragment())
                }
                true
            }
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun replaceFragment(frag: Fragment) {
        bottomNavView.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, frag)
            .commit()
    }
}

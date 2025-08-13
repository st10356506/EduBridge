package com.example.edubridge

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottom_navigation)
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment()) // Android Developers (n.d.). Fragments. Available at: https://developer.android.com/guide/fragments
            bottomNavView.selectedItemId = R.id.nav_dashboard
        }
        bottomNavView.setOnItemSelectedListener {
         when(it.itemId){
             R.id.nav_dashboard -> replaceFragment(HomeFragment())
             R.id.nav_study -> replaceFragment(StudyFragment())
             R.id.nav_chat -> replaceFragment(AiFragment())
             R.id.nav_scan -> replaceFragment(ScanFragment())
             R.id.nav_rewards -> replaceFragment(RewardsFragment())
         }
            true
        }
    }
    private fun replaceFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, frag)
            .commit() // Android Developers (n.d.). Fragments. Available at: https://developer.android.com/guide/fragments
    }
}
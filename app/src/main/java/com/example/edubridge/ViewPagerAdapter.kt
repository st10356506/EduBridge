package com.example.edubridge

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// This is the version you originally had, which is correct if used within a Fragment
class ViewPagerAdapter(fragment: Fragment, private val itemCount: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllSubjectsFragment() // Make sure this fragment exists
            1 -> RecentFragment()     // Make sure this fragment exists
            2 -> SavedFragment()      // Make sure this fragment exists
            else -> throw IllegalArgumentException("Invalid position: $position. Expected 0 to ${itemCount - 1}")
        }
    }
}
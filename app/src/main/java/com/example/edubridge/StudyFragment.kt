package com.example.edubridge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.edubridge.ViewPagerAdapter
import com.example.edubridge.databinding.FragmentStudyMaterialsBinding
import com.google.android.material.tabs.TabLayoutMediator

class StudyFragment : Fragment() {

    private var _binding: FragmentStudyMaterialsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudyMaterialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerWithTabs()
    }

    private fun setupViewPagerWithTabs() {
        val tabTitles = arrayOf("All Subjects", "Recent", "Saved")
        val adapter = ViewPagerAdapter(this, tabTitles.size)
        binding.viewPager.adapter = adapter

        // This is a common point of failure. Ensure both viewPager and tabLayout are not null.
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

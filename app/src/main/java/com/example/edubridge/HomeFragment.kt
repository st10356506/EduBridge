package com.example.edubridge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // This is where you connect the layout file to the fragment
        return inflater. inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the logout button in the layout using its ID
        val logoutButton = view.findViewById<Button>(R.id.logout_button)
        logoutButton?.setOnClickListener {
            // Get SharedPreferences and clear the user data
            val sharedPref = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            sharedPref?.edit()?.clear()?.apply()

            // Navigate back to the LoginActivity and finish MainActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
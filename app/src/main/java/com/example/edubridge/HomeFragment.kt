package com.example.edubridge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get reference to the welcome text
        val welcomeTextView = view.findViewById<TextView>(R.id.welcome_text)

        // Load username/email from SharedPreferences
        val sharedPref = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("user_username", null)
        val email = sharedPref?.getString("user_email", null)

        // Show "Welcome back, username" (fallback to email if username is null)
        welcomeTextView?.text = "Welcome back, ${username ?: email ?: "User"}"

        // Find the logout button in the layout using its ID
        val logoutButton = view.findViewById<Button>(R.id.logout_button)
        logoutButton?.setOnClickListener {
            // Clear SharedPreferences
            sharedPref?.edit()?.clear()?.apply()

            // Navigate back to the LoginActivity and finish MainActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}

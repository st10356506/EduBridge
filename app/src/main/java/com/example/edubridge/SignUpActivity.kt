package com.example.edubridge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_signup) // Inflate the layout

        val usernameEditText = findViewById<EditText>(R.id.username_edit_text) // ✅ new field
        val emailEditText = findViewById<EditText>(R.id.email_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirm_password_edit_text)
        val signUpButton = findViewById<Button>(R.id.sign_up_button)
        val loginTextView = findViewById<TextView>(R.id.login_text_view)

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the user's credentials (including username) to SharedPreferences
            val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("user_username", username)
                putString("user_email", email)
                putString("user_password", password)
                apply()
            }

            Log.d("SignUpActivity", "Account created for $username ($email)")

            // Navigate back to the LoginActivity so the user can log in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginTextView.setOnClickListener {
            // Navigate back to the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

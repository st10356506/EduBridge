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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        val emailEditText = findViewById<EditText>(R.id.email_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        val loginButton = findViewById<Button>(R.id.login_button)
        val signUpTextView = findViewById<TextView>(R.id.sign_up_text_view)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val savedEmail = sharedPref.getString("user_email", "")
            val savedPassword = sharedPref.getString("user_password", "")
            val savedUsername = sharedPref.getString("user_username", "") // ✅ get username

            val loginSuccessful = (email == savedEmail && password == savedPassword)

            if (loginSuccessful) {
                Log.d("LoginActivity", "Login successful for email: $email")

                // Pass username to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", savedUsername) // ✅ send username
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Login failed for email: $email")
            }
        }

        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}

package com.example.socialnetworkmobile.ui.registration

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.socialnetworkmobile.databinding.ActivityRegistrationBinding
import com.example.socialnetworkmobile.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val registerViewModel: RegistrationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (validateInputs(username, email, password, confirmPassword)) {
                registerViewModel.register(username, email, password).observe(this, Observer { result ->
                    result.onSuccess { response ->
                        setFieldsEnabled(false)
                        Toast.makeText(this, response?.message, Toast.LENGTH_LONG).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            finish()
                        }, 1500)
                    }.onFailure { exception ->
                        Toast.makeText(this, exception.message ?: "Registration failed!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        setupLoginLinkAction()
    }

    private fun validateInputs(username: String, email: String, password: String, confirmPassword: String ): Boolean {
        if (username.isBlank() || username.length !in 3..20) {
            Toast.makeText(this, "Username must be between 3 and 20 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email should be valid", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isBlank() || !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?])(?=\\S+$).{8,}$".toRegex())) {
            println(password)
            Toast.makeText(this, "Password must be at least 8 characters long, contain at least 1 digit, 1 lower case, 1 upper case, 1 special character and no whitespace", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    private fun setFieldsEnabled(enabled: Boolean) {
        binding.username.isEnabled = enabled
        binding.email.isEnabled = enabled
        binding.password.isEnabled = enabled
        binding.confirmPassword.isEnabled = enabled
        binding.registerButton.isEnabled = enabled
        binding.loginLink.isEnabled = enabled
    }
    private fun setupLoginLinkAction() {
        binding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
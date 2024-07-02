package com.example.socialnetworkmobile.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetworkmobile.databinding.ActivityLoginBinding
import androidx.lifecycle.Observer
import com.example.socialnetworkmobile.MainActivity
import com.example.socialnetworkmobile.ui.registration.RegistrationActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefilledUsername = intent.getStringExtra("username")
        if (!prefilledUsername.isNullOrEmpty()) {
            binding.username.setText(prefilledUsername)
        }
        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            if (validateInputs(username, password)) {
                loginViewModel.login(username, password).observe(this, Observer { result ->
                    result.onSuccess { jwt ->
                        jwt.let {
                            saveUserInfo(username, it.toString())
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }.onFailure { exception ->
                        Toast.makeText(this, exception.message ?: "Login failed!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        setupRegisterLinkAction();
    }
    private fun validateInputs(username: String, password: String): Boolean {
        if (username.isBlank()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isBlank()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun saveUserInfo(username: String, token: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("jwt_token", token)
        editor.apply()
    }

    private fun setupRegisterLinkAction() {
        binding.registerLink.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
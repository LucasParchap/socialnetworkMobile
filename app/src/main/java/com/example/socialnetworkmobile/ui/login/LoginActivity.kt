package com.example.socialnetworkmobile.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetworkmobile.MainActivity
import com.example.socialnetworkmobile.databinding.ActivityLoginBinding
import com.example.socialnetworkmobile.ui.registration.RegistrationActivity


class LoginActivity : AppCompatActivity(){

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.loginButton.setOnClickListener  {
            val username = loginBinding.username.text.toString()
            val password = loginBinding.password.text.toString()
            if (username == "user" && password == "password") {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
            }
        }
        loginBinding.registerLink.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
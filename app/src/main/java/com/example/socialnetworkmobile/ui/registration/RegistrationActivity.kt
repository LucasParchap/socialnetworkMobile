package com.example.socialnetworkmobile.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetworkmobile.databinding.ActivityRegistrationBinding
import com.example.socialnetworkmobile.ui.login.LoginActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var registrationBinding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        registrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(registrationBinding.root)

        registrationBinding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
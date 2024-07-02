package com.example.socialnetworkmobile.ui.logout

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel

class LogoutViewModel : ViewModel() {

    fun logout(context : Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
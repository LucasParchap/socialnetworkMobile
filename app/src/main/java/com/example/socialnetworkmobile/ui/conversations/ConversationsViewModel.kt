package com.example.socialnetworkmobile.ui.conversations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConversationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is conversations Fragment"
    }
    val text: LiveData<String> = _text
}
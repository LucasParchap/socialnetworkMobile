package com.example.socialnetworkmobile.di.modules

import com.example.socialnetworkmobile.MainActivityViewModel
import com.example.socialnetworkmobile.ui.conversations.ConversationsViewModel
import com.example.socialnetworkmobile.ui.home.HomeViewModel
import com.example.socialnetworkmobile.ui.login.LoginViewModel
import com.example.socialnetworkmobile.ui.logout.LogoutViewModel
import com.example.socialnetworkmobile.ui.notifications.NotificationsViewModel
import com.example.socialnetworkmobile.ui.registration.RegistrationViewModel
import com.example.socialnetworkmobile.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModels = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel{ HomeViewModel() }
    viewModel{ ConversationsViewModel() }
    viewModel{ NotificationsViewModel() }
    viewModel{ SettingsViewModel() }
    viewModel{ LogoutViewModel() }
    viewModel{ MainActivityViewModel( get() ) }
}
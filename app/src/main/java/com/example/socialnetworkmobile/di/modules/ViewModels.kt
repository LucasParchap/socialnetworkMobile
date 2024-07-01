package com.example.socialnetworkmobile.di.modules

import com.example.socialnetworkmobile.ui.login.LoginViewModel
import com.example.socialnetworkmobile.ui.registration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModels = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get())}
}
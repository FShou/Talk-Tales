package com.capstone.talktales.ui.splash

import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.AuthRepository

class SplashViewModel (
    private val authRepository: AuthRepository): ViewModel() {
    fun getLoginUser() = authRepository.getLoginUser()
}
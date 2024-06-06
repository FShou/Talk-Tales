package com.capstone.talktales.ui.register

import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = authRepository.register(name, email, password)
}
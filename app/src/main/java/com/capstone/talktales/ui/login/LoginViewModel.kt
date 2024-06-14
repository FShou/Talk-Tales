package com.capstone.talktales.ui.login

import android.media.session.MediaSession.Token
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.talktales.data.model.User
import com.capstone.talktales.data.repo.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(email: String, password: String) = authRepository.login(email, password)
    fun saveLoginUser(user: User) = viewModelScope.launch {
        authRepository.saveLoginUser(user.name, user.email, user.token)
    }

}
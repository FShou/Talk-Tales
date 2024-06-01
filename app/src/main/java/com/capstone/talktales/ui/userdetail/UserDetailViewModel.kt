package com.capstone.talktales.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.talktales.data.repo.UserRepository
import kotlinx.coroutines.launch

class UserDetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun logout() = viewModelScope.launch {
        userRepository.clearLoginUser()
    }
}
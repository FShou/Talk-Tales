package com.capstone.talktales.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.talktales.data.repo.UserRepository
import com.capstone.talktales.factory.UserViewModelFactory
import kotlinx.coroutines.launch

class UserDetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun logout() = viewModelScope.launch {
        userRepository.clearLoginUser()
        UserViewModelFactory.destroy()
    }

    fun getLoginUser() = userRepository.getLoginUser()

}
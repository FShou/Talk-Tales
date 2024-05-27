package com.capstone.talktales.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.talktales.data.repo.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getStories() = userRepository.getStories()
}
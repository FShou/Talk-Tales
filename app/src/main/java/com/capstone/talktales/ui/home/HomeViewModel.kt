package com.capstone.talktales.ui.home

import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.UserRepository

class HomeViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getStories() = userRepository.getStories()
}
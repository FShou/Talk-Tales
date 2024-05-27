package com.capstone.talktales.ui.story

import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.UserRepository

class StoryViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getStories() = userRepository.getStories()
    fun getStoryDetail(id: String) = userRepository.getStoryDetail(id)

}
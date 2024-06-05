package com.capstone.talktales.ui.storydetail

import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.UserRepository

class StoryDetailViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getStoryDetail(id: String) = userRepository.getStoryDetail(id)

}
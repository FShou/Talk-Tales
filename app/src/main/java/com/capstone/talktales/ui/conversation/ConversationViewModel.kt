package com.capstone.talktales.ui.conversation

import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.UserRepository

class ConversationViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getConversation(storyId: String) = userRepository.getConversation(storyId)
}
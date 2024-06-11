package com.capstone.talktales.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.UserRepository
import okhttp3.MultipartBody

class ConversationViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _storyLogId = MutableLiveData<Int>()
    // Setter method to update the storyLogId
    fun setStoryLogId(id: Int) {
        _storyLogId.value = id
    }

    // Getter method to get the current value of storyLogId
    fun getStoryLogId(): Int? {
        return _storyLogId.value
    }
    fun predictUserAudio(storyLogId: Int, storyConvId: Int, file: MultipartBody.Part) = userRepository.predictUserAudio(storyLogId,storyConvId, file)

    fun getConversation(storyId: String) = userRepository.getConversation(storyId)
}
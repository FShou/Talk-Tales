package com.capstone.talktales.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.remote.response.PredictionData
import com.capstone.talktales.data.repo.UserRepository
import okhttp3.MultipartBody

class ConversationViewModel(private val userRepository: UserRepository): ViewModel() {

    private var _storyLogId: Int = 0
    fun setStoryLogId(id: Int) {
        _storyLogId = id
    }

    private var _page = MutableLiveData(0)
    val page: LiveData<Int> = _page

    fun nextPage() {
        _page.value = _page.value?.plus(1)
    }

    fun getStoryLogId(): Int {
        return _storyLogId
    }

    private val _feedback = MutableLiveData<PredictionData?>()
    val feedback: LiveData<PredictionData?> get() = _feedback

    fun setFeedback(predictionData: PredictionData?) {
        _feedback.value = predictionData
    }

    fun predictUserAudio(storyLogId: Int, storyConvId: Int, file: MultipartBody.Part) = userRepository.predictUserAudio(storyLogId,storyConvId, file)

    fun getConversation(storyId: String) = userRepository.getConversation(storyId)
}
package com.capstone.talktales.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.remote.response.PredictionData
import com.capstone.talktales.data.repo.ModelRepository
import com.capstone.talktales.data.repo.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ConversationViewModel(private val userRepository: UserRepository, private val modelRepository: ModelRepository): ViewModel() {

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

    private val _feedback = MutableLiveData<PredictionData?>(null)
    val feedback: LiveData<PredictionData?> get() = _feedback

    fun setFeedback(predictionData: PredictionData?) {
        _feedback.value = predictionData
    }

    fun predictUserAudio(storyLogId: Int, storyConvId: Int, file: MultipartBody.Part) = userRepository.predictUserAudio(storyLogId,storyConvId, file)

    fun predictUserAudio(file: MultipartBody.Part, target: RequestBody)  = modelRepository.predictUserAudio(file, target)


    fun getConversation(storyId: String) = userRepository.getConversation(storyId)
}
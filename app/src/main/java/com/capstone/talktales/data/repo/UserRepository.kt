package com.capstone.talktales.data.repo

import androidx.lifecycle.asLiveData
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.remote.retrofit.UserApiService
import okhttp3.MultipartBody

class UserRepository(
    private val userApiService: UserApiService,
    private val userPref: UserPreference
): BaseRepository() {

    suspend fun clearLoginUser() = userPref.clearLoginUser()

    fun getLoginUser() = userPref.getLoginUser().asLiveData()

    fun getStories() = callApiWrapped { userApiService.getStories() }

    fun getStoryDetail(id: String) = callApiWrapped { userApiService.getStoryDetail(id) }

    fun predictUserAudio(storyLogId: Int, storyConvId: Int, file: MultipartBody.Part) = callApiWrapped { userApiService.predictUserAudio(storyLogId, storyConvId, file) }

    fun getConversation(storyId: String) = callApiWrapped { userApiService.getConversationByStoryId(storyId) }


}

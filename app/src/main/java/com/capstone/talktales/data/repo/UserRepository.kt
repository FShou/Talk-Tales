package com.capstone.talktales.data.repo

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.data.remote.retrofit.ApiService
import kotlinx.coroutines.delay
import okhttp3.MultipartBody

class UserRepository(
    private val apiService: ApiService,
    private val userPref: UserPreference
): BaseRepository() {

    suspend fun clearLoginUser() = userPref.clearLoginUser()

    fun getStories() = callApiWrapped { apiService.getStories() }

    fun getStoryDetail(id: String) = callApiWrapped { apiService.getStoryDetail(id) }

    fun predictUserAudio(storyLogId: Int, storyConvId: Int, file: MultipartBody.Part) = callApiWrapped { apiService.predictUserAudio(storyLogId, storyConvId, file) }

    fun getConversation(storyId: String) = callApiWrapped { apiService.getConversationByStoryId(storyId) }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, userPref: UserPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPref)
            }.also { instance = it }

    }
}

package com.capstone.talktales.data.repo

import androidx.lifecycle.asLiveData
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.remote.retrofit.UserApiService

class UserRepository(
    private val userApiService: UserApiService,
    private val userPref: UserPreference
): BaseRepository() {

    suspend fun clearLoginUser() = userPref.clearLoginUser()

    fun getLoginUser() = userPref.getLoginUser().asLiveData()

    fun getStories() = callApiWrapped { userApiService.getStories() }

    fun getStoryDetail(id: String) = callApiWrapped { userApiService.getStoryDetail(id) }

    fun getConversation(storyId: String) = callApiWrapped { userApiService.getConversationByStoryId(storyId) }


}

package com.capstone.talktales.data.repo

import androidx.lifecycle.asLiveData
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.remote.retrofit.ApiService

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val userPref: UserPreference
) : BaseRepository() {
    fun getLoginUser() = userPref.getLoginUser().asLiveData()
    suspend fun saveLoginUser(name:String, email:String, token: String) = userPref.saveLoginUser(name, email, token)

    fun login(email: String, password: String) =
        callApiWrapped { apiService.login(email, password) }


    fun register(name: String, email: String, password: String) =
        callApiWrapped { apiService.register(name, email, password) }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiService: ApiService, userPref: UserPreference): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPref)
            }.also { instance = it }

    }
}
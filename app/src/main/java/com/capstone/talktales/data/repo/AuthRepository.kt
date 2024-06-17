package com.capstone.talktales.data.repo

import androidx.lifecycle.asLiveData
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.remote.retrofit.AuthApiService

class AuthRepository private constructor(
    private val apiService: AuthApiService,
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

        fun getInstance(authApiService: AuthApiService, userPref: UserPreference): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(authApiService, userPref)
            }.also { instance = it }

    }
}
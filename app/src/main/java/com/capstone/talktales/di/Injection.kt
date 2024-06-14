package com.capstone.talktales.di

import android.content.Context
import com.capstone.talktales.data.repo.UserRepository
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.local.preference.dataStore
import com.capstone.talktales.data.remote.retrofit.ApiConfig
import com.capstone.talktales.data.repo.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)

        val token: String = runBlocking { pref.getLoginUser().first().token}

        val apiService = ApiConfig.getApiService(context, token)

        return AuthRepository.getInstance(apiService, pref)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)

        val token: String = runBlocking { pref.getLoginUser().first().token }

        val apiService = ApiConfig.getApiService(context, token)

        return UserRepository(apiService, pref)
    }
}
package com.capstone.talktales.di

import android.content.Context
import com.capstone.talktales.data.repo.UserRepository
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.local.preference.dataStore
import com.capstone.talktales.data.remote.retrofit.ApiConfig
import com.capstone.talktales.data.repo.AuthRepository
import com.capstone.talktales.data.repo.ModelRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)

        val authApiService = ApiConfig.getAuthApiService()

        return AuthRepository.getInstance(authApiService, pref)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)

        val token: String = runBlocking { pref.getLoginUser().first().token }

        val userApiService = ApiConfig.getUserApiService(context, token)

        return UserRepository(userApiService, pref)
    }

    fun provideModelRepository(): ModelRepository {
        val modelApiService = ApiConfig.getModelApiService()

        return  ModelRepository.getInstance(modelApiService)
    }
}
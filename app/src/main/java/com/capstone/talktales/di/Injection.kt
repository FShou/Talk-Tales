package com.capstone.talktales.di

import android.content.Context
import com.capstone.talktales.data.MainRepository
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.local.preference.dataStore
import com.capstone.talktales.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): MainRepository {
        // TODO: DataStore for saving User Token
        val pref = UserPreference.getInstance(context.dataStore)
        val token: String = runBlocking { pref.getLoginUser().first().toString() }

        val apiService = ApiConfig.getApiService(context, token)

        return MainRepository.getInstance(apiService, pref)
    }
}
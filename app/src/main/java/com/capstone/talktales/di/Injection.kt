package com.capstone.talktales.di

import android.content.Context
import com.capstone.talktales.data.MainRepository
import com.capstone.talktales.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getApiService(context)

        return MainRepository.getInstance(apiService)
    }
}
package com.capstone.talktales.data.repo

import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.remote.retrofit.AuthApiService
import com.capstone.talktales.data.remote.retrofit.ModelApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ModelRepository private constructor(private val modelApiService: ModelApiService): BaseRepository() {

    fun predictUserAudio(file: MultipartBody.Part, target: RequestBody) = callApiWrapped { modelApiService.predictUserAudio( file, target) }


    companion object {
        @Volatile
        private var instance: ModelRepository? = null

        fun getInstance(modelApiService: ModelApiService): ModelRepository =
            instance ?: synchronized(this) {
                instance ?: ModelRepository(modelApiService)
            }.also { instance = it }
    }
}
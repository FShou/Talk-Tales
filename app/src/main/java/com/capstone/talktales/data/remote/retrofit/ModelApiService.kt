package com.capstone.talktales.data.remote.retrofit

import com.capstone.talktales.data.remote.response.CheckAudioResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ModelApiService {
    @Multipart
    @POST("/api/predict/")
    suspend fun predictUserAudio(
        @Part file: MultipartBody.Part,
        @Part("target") target: RequestBody
    ): CheckAudioResponse
}
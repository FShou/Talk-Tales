package com.capstone.talktales.data.remote.retrofit

import android.content.Context
import androidx.media3.common.BuildConfig
import com.mustafayigit.mockresponseinterceptor.MockResponseInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    fun getApiService(context: Context): ApiService{

        val BASE_URL = "https://localhost/"

        val loggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        } else loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)


        // mock API Response
        val mockInterceptor = MockResponseInterceptor.Builder(context.assets)
            .isGlobalMockingEnabled { true }
            .build()



        val client  = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
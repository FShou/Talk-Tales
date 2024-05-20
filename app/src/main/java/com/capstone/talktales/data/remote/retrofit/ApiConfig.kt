package com.capstone.talktales.data.remote.retrofit

import android.content.Context
import androidx.media3.common.BuildConfig
import com.mustafayigit.mockresponseinterceptor.MockResponseInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    fun getApiService(context: Context, token: String): ApiService{

        // Todo: get user token for Header Interceptor

       // Todo: move to buildConfig
        val BASE_URL = "https://localhost/"

        val loggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        } else loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)


        // mock API Response
        // Todo: Remove when Real-API implemented
        val mockInterceptor = MockResponseInterceptor.Builder(context.assets)
            .isGlobalMockingEnabled { true }
            .build()


        // Todo: add header Interceptor for Authentication



        val client  = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)         // Todo: Remove when Real-API implemented
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
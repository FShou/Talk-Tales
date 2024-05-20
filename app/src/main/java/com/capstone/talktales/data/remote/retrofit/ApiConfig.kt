package com.capstone.talktales.data.remote.retrofit

import android.content.Context
import android.util.Log
import com.capstone.talktales.BuildConfig
import com.mustafayigit.mockresponseinterceptor.MockResponseInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    fun getApiService(context: Context, token: String): ApiService {

        val loggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        else
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)


        // mock API Response
        // Todo: Remove when Real-API implemented
        val mockInterceptor = MockResponseInterceptor.Builder(context.assets)
            .isGlobalMockingEnabled { true }
            .build()


        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(mockInterceptor)         // Todo: Remove when Real-API implemented
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
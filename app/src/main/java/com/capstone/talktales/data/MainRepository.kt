package com.capstone.talktales.data

import android.util.Log
import androidx.lifecycle.liveData
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.data.remote.retrofit.ApiService
import kotlinx.coroutines.delay

class MainRepository(
    private val apiService: ApiService
) {

    fun getStories() = liveData {
        emit(ResponseResult.Loading)
        delay(3000)
        try {
            val response = apiService.getStoryDetail("s")
            emit(ResponseResult.Success(response))
        } catch (e: Exception) {
            e.message
                ?.let { ResponseResult.Error(it) }
                ?.let {
                    emit(it)
                    Log.e("REPO",it.msg)
                }
        }
    }


    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(apiService: ApiService): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService)
            }.also { instance = it }

    }
}

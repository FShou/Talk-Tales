package com.capstone.talktales.data

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.capstone.talktales.data.local.preference.UserPreference
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.data.remote.retrofit.ApiService
import kotlinx.coroutines.delay
import okhttp3.MultipartBody

class MainRepository(
    private val apiService: ApiService,
    private val userPref: UserPreference
) {

    fun getLoginUser() = userPref.getLoginUser().asLiveData()
    suspend fun saveLoginUser(token: String) = userPref.saveLoginUser(token)
    suspend fun clearLoginUser() = userPref.clearLoginUser()

    fun login(email: String, password: String) = callApiWrapped { apiService.login(email, password ) }


    fun register(name: String, email: String, password: String ) = callApiWrapped { apiService.register(name, email, password) }

    fun getStories() = callApiWrapped { apiService.getStories() }

    fun getStoryDetail(id: String) = callApiWrapped { apiService.getStoryDetail(id) }

    fun checkSceneAudio(audioFile: MultipartBody.Part) = callApiWrapped { apiService.checkUserAudio(audioFile) }



    /**
     * Bungkus api call pake LiveData & ResponseResult
     * pake nya kaya gini :
     * ```
     * liveData.observe(this, Observer { result ->
     *       when(result) {
     *           is ResponseResult.Loading -> {
     *               // Show loading spinner
     *           }
     *           is ResponseResult.Success -> {
     *               // Handle the success result
     *               val data = result.data
     *           }
     *           is ResponseResult.Error -> {
     *               // Handle the error
     *               val errorMessage = result.msg
     *           }
     *       }
     * ```
     * @param apiCall ambil dari ApiService
    */
    private inline fun <T> callApiWrapped(crossinline apiCall: suspend () -> T) = liveData<ResponseResult<T>> {
        emit(ResponseResult.Loading)
        delay(3000) // Todo: Remove this on Prod
        try {
            val response = apiCall()
            emit(ResponseResult.Success(response))
        } catch (e: Exception) {
            e.message
                ?.let { ResponseResult.Error(it) }
                ?.let {
                    emit(it)
                    Log.e("REPO", it.msg)
                }
        }
    }


    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(apiService: ApiService, userPref: UserPreference): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService, userPref)
            }.also { instance = it }

    }
}

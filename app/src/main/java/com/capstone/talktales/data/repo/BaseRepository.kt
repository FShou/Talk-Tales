package com.capstone.talktales.data.repo

import android.util.Log
import androidx.lifecycle.liveData
import com.capstone.talktales.data.remote.response.ErrorResponse
import com.capstone.talktales.data.remote.response.ResponseResult
import com.google.gson.Gson
import retrofit2.HttpException

abstract class BaseRepository {

    /**
     * Bungkus api call pake LiveData & ResponseResult
     * pake nya kaya gini :
     * ```
     * liveData.observe(this) { result ->
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
     protected inline fun <T> callApiWrapped(crossinline apiCall: suspend () -> T) = liveData<ResponseResult<T>> {
        emit(ResponseResult.Loading)
        try {
            val response = apiCall()
            emit(ResponseResult.Success(response))
        } catch (e: HttpException) {
            val jsonBody = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonBody, ErrorResponse::class.java)
            errorBody?.let {
                emit(ResponseResult.Error(it.message))
            }
        }
        catch (e: Exception) {
            Log.e("REPO", e.toString())
            e.message
                ?.let { ResponseResult.Error(it) }
                ?.let {
                    emit(it)
                }
        }
    }
}
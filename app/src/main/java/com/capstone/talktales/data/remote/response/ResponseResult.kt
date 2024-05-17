package com.capstone.talktales.data.remote.response

sealed class ResponseResult<out R> private constructor(){
    data object Loading: ResponseResult<Nothing>()
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val msg: String) : ResponseResult<Nothing>()
}
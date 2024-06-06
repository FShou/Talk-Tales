package com.capstone.talktales.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class User(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String
)

data class Data(

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("token")
    val token: String
)
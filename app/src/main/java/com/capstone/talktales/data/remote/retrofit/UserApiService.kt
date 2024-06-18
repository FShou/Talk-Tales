package com.capstone.talktales.data.remote.retrofit

import com.capstone.talktales.data.remote.response.ConversationResponse
import com.capstone.talktales.data.remote.response.DetailStoryResponse
import com.capstone.talktales.data.remote.response.LoginResponse
import com.capstone.talktales.data.remote.response.RegisterResponse
import com.capstone.talktales.data.remote.response.StoriesResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {


    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("/auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("/story")
    suspend fun getStories(): StoriesResponse

    @GET("/story/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): DetailStoryResponse

    @GET("/conversation/{storyId}")
    suspend fun getConversationByStoryId(
        @Path("storyId") storyId : String
    ): ConversationResponse


}
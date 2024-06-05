package com.capstone.talktales.data.remote.retrofit

import com.capstone.talktales.data.remote.response.BaseResponse
import com.capstone.talktales.data.remote.response.CheckAudioResponse
import com.capstone.talktales.data.remote.response.DetailStoryResponse
import com.capstone.talktales.data.remote.response.LoginResponse
import com.capstone.talktales.data.remote.response.RegisterResponse
import com.capstone.talktales.data.remote.response.StoriesResponse
import com.mustafayigit.mockresponseinterceptor.Mock
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // Todo: Implement real API Spec

    @FormUrlEncoded
    @POST("/login")
    @Mock
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @FormUrlEncoded
    @POST("/register")
    @Mock
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("/story")
    @Mock
    suspend fun getStories(): StoriesResponse

    @GET("/story/{id}")
    @Mock
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("/stories/{id}/{scene}")
    @Mock
    suspend fun checkUserAudio(
        @Part file: MultipartBody.Part,

        // Todo: add additional parameter
    ): CheckAudioResponse


}
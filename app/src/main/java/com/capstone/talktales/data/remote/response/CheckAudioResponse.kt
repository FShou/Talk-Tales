package com.capstone.talktales.data.remote.response

import com.google.gson.annotations.SerializedName

data class CheckAudioResponse(

    @field:SerializedName("data")
    val data: PredictionData? = null,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String? = null
)

data class PredictionData(

    @field:SerializedName("feedback")
    val feedback: String,

    @field:SerializedName("scores")
    val scores: Int,

    @field:SerializedName("file_name")
    val fileName: String? = null,

    @field:SerializedName("prediction")
    val prediction: String? = null,

    @field:SerializedName("idLogConversation")
    val idLogConversation: Int? = null,

    @field:SerializedName("target")
    val target: String? = null
)

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

    @field:SerializedName("markup_html")
    val html: String,

    )

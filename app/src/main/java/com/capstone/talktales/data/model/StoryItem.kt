package com.capstone.talktales.data.model

import com.google.gson.annotations.SerializedName

data class StoryItem(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("thumbnail_url")
    val imgUrl: String,

    @field:SerializedName("title")
    val title: String,
)
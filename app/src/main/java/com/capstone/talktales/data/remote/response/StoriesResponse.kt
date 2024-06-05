package com.capstone.talktales.data.remote.response

import com.capstone.talktales.data.model.Story
import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @field:SerializedName("data")
    val listStory: List<Story>
)



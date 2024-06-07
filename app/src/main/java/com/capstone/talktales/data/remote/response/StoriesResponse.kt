package com.capstone.talktales.data.remote.response

import com.capstone.talktales.data.model.StoryItem
import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @field:SerializedName("data")
    val listStoryItem: List<StoryItem>
)



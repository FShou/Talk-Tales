package com.capstone.talktales.data.remote.response

import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    // Todo: match API spec
    @field:SerializedName("list-story")
    val listStory: List<Story>
)


// Todo: move to model pkg
data class Story(
    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("img-url")
    val imgUrl: String,

    @field:SerializedName("title")
    val title: String
)

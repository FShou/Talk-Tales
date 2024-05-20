package com.capstone.talktales.data.remote.response

data class StoriesResponse(
    // Todo: match API spec
    val listStory: Story
)

data class Story (
    val title: String
)

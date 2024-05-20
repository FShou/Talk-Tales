package com.capstone.talktales.data.remote.response

data class DetailStoryResponse (
    // Todo: match API spec

    val scenes : Scene
)

data class Scene(
    val id : String
)
package com.capstone.talktales.data.model

import com.google.gson.annotations.SerializedName

data class Story(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("thumbnail_url")
    val imgUrl: String,

    @field:SerializedName("title")
    val title: String,

//    @field:SerializedName("synopsis")
//    val synopsis: String,
//
//    @field:SerializedName("Glosarium")
//    val listGlossary: List<Glossary>
)
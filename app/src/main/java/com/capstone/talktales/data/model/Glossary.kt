package com.capstone.talktales.data.model

import com.google.gson.annotations.SerializedName

data class Glossary(
    @field:SerializedName("word")
    val word: String,

    @field:SerializedName("explanation")
    val explanation: String,
)
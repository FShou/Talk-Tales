package com.capstone.talktales.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Glossary(
    @field:SerializedName("word")
    val word: String,

    @field:SerializedName("explanation")
    val explanation: String,
): Parcelable
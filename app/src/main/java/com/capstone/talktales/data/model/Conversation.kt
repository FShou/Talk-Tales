package com.capstone.talktales.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Conversation(

	@field:SerializedName("storyId")
	val storyId: Int,

	@field:SerializedName("prolog_text")
	val prologText: String? = null,

	@field:SerializedName("characer_name")
	val characterName: String? = null,

	@field:SerializedName("voice_url")
	val voiceUrl: String? = null,

	@field:SerializedName("character_img")
	val characterImg: String? = null,


	@field:SerializedName("conv_sequence")
	val convSequence: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("conv_text")
	val convText: String? = null,

	@field:SerializedName("is_speech_by_user")
	val isSpeechByUser: Boolean
): Parcelable
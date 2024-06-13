package com.capstone.talktales.data.remote.response

import com.capstone.talktales.data.model.Conversation
import com.google.gson.annotations.SerializedName

data class ConversationResponse(

	@field:SerializedName("data")
	val data: ConversationData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ConversationData(

	@field:SerializedName("storyLogId")
	val storyLogId: Int,

	@field:SerializedName("conversations")
	val conversations: List<Conversation>
)


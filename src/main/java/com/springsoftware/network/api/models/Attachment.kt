package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("items") val items: List<AttachmentItem>,
    @SerializedName("next_from") val nextFrom: String?
)

data class AttachmentItem(
    @SerializedName("message_id") val messageId: Int,
    @SerializedName("from_id") val fromId: Int,
    @SerializedName("attachment") val attachment: Attachment
)
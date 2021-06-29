package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class MessagesResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Message>,
    @SerializedName("profiles") val profiles: List<User>?,
    @SerializedName("groups") val groups: List<Group>?
)
data class Message(
    @SerializedName("date") val date: Int,
    @SerializedName("from_id") val fromId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("out") val out: Int,
    @SerializedName("peer_id") val peerId: Int,
    @SerializedName("text") val text: String,
    @SerializedName("conversation_message_id") val conversationMessageId: Int,
    @SerializedName("fwd_messages") val fwdMessages: List<Message>?,
    @SerializedName("reply_message") val replyMessage: Message?,
    @SerializedName("action") val action: Action?,
    @SerializedName("important") val important: Boolean,
    @SerializedName("random_id") val randomId: Int,
    @SerializedName("attachments") val attachments: List<Attachment>,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("update_time") val updateTime: Int
)
data class Action(
    @SerializedName("type") val type: String,
    @SerializedName("member_id") val memberId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("date") val date: Int,
    @SerializedName("photo") val photo: Covers?
)
data class Covers(
    @SerializedName("photo_50") val photo50: String,
    @SerializedName("photo_100") val photo100: String,
    @SerializedName("photo_200") val photo200: String
)
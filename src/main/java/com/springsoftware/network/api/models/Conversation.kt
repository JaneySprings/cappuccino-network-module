package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class ConversationsByIdResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Conversation>,
    @SerializedName("profiles") val profiles: List<User>?,
    @SerializedName("groups") val groups: List<Group>?
)
data class ConversationsResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<ConversationSet>,
    @SerializedName("profiles") val profiles: List<User>?,
    @SerializedName("groups") val groups: List<Group>?
)

data class ConversationSet(
    @SerializedName("conversation") val conversation: Conversation,
    @SerializedName("last_message") val lastMessage: Message
)
data class Conversation(
    @SerializedName("peer") val peer: Peer,
    @SerializedName("last_message_id") val lastMessageId: Int,
    @SerializedName("in_read") val inRead: Int,
    @SerializedName("out_read") val outRead: Int,
    @SerializedName("unread_count") val unreadCount: Int,
    @SerializedName("important") val important: Boolean,
    @SerializedName("unanswered") val unanswered: Boolean,
    @SerializedName("push_settings") val pushSettings: PushSettings?,
    @SerializedName("can_write") val canWrite: CanWrite,
    @SerializedName("chat_settings") val chatSettings: ChatSettings?
)
data class Peer(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("local_id") val local_id: Int
)
data class PushSettings(
    @SerializedName("disabled_until") val disabledUntil: Int,
    @SerializedName("disabled_forever") val disabled_forever: Boolean,
    @SerializedName("no_sound") val no_sound: Boolean
)
data class CanWrite(
    @SerializedName("allowed") val allowed: Boolean,
    @SerializedName("reason") val reason: Int
)
data class ChatSettings(
    @SerializedName("members_count") val membersCount: Int,
    @SerializedName("title") val title: String,
    @SerializedName("pinned_message") val pinnedMessage: Message?,
    @SerializedName("state") val state: String,
    @SerializedName("photo") val photo: ChatPhotos?,
    @SerializedName("active_ids") val activeIds: List<Int>,
    @SerializedName("is_group_channel") val isGroupChannel: Boolean
)
data class ChatPhotos(
    @SerializedName("photo_50") val photo50: String,
    @SerializedName("photo_100") val photo100: String,
    @SerializedName("photo_200") val photo200: String
)
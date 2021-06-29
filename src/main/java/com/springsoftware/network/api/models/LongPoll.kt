package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class LongPollResponse(
    @SerializedName("history") val history: List<List<Int>>,
    @SerializedName("messages") val messages: Messages,
    @SerializedName("profiles") val profiles: List<User>?,
    @SerializedName("groups") val groups: List<Group>?,
    @SerializedName("new_pts") val newPts: Int
)
data class Messages(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Message>
)
package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class Counter (
    @SerializedName("friends") val friends: Int,
    @SerializedName("messages") val messages: Int = 0,
    @SerializedName("photos") val photos: Int = 0,
    @SerializedName("videos") val videos: Int = 0,
    @SerializedName("gifts") val gifts: Int = 0,
    @SerializedName("events") val events: Int = 0,
    @SerializedName("groups") val groups: Int = 0,
    @SerializedName("notifications") val notifications: Int = 0,
    @SerializedName("friends_recommendations") val friendsRecommendations: Int = 0,
    @SerializedName("menu_discover_badge") val discoverBadge: Int = 0
)
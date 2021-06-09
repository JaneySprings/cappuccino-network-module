package com.springsoftware.network.api.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class VideoResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Video>
)

@Parcelize
data class Video(
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("can_add") val canAdd: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("image") val images: List<Image>,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("player") val player: String?,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("views") val views: Int
): Parcelable
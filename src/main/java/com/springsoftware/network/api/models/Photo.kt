package com.springsoftware.network.api.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/* Photo model */
data class PhotosResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Photo>
)

@Parcelize
data class Photo(
    @SerializedName("album_id") val albumId: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("access_key") val accessKey: String?,
    @SerializedName("sizes") val sizes: List<Size>,
    @SerializedName("text") val text: String?
): Parcelable

@Parcelize
data class Size(
    @SerializedName("height") val height: Int,
    @SerializedName("url", alternate = ["src"]) val url: String,
    @SerializedName("type") val type: String,
    @SerializedName("width") val width: Int
): Parcelable
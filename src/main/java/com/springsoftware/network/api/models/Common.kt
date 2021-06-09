package com.springsoftware.network.api.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Attachment(
    @SerializedName("type") val type: String,
    @SerializedName("photo") val photo: Photo?,
    @SerializedName("video") val video: Video?,
    @SerializedName("audio") val audio: Audio?,
    @SerializedName("doc") val doc: Document?,
    @SerializedName("link") val link: Link?,
    @SerializedName("sticker") val sticker: Sticker?,
    @SerializedName("graffiti") val graffiti: Graffiti?,
    @SerializedName("audio_message") val audioMessage: AudioMessage?,
    @SerializedName("wall") val wall: Repost?
)
data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
)
data class Country(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
)
@Parcelize
data class Image(
    @SerializedName("height") val height: Int,
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int
): Parcelable
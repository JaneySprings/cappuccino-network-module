package com.springsoftware.network.api.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DocsResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Document>
)


@Parcelize
data class Document(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("access_key") val accessKey: String?,
    @SerializedName("title") val title: String,
    @SerializedName("size") val size: Int,
    @SerializedName("ext") val ext: String,
    @SerializedName("url") val url: String,
    @SerializedName("date") val date: Int,
    @SerializedName("type") val type: Int,
    @SerializedName("preview") val preview: Preview?
): Parcelable

@Parcelize
data class Graffiti(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("access_key") val accessKey: String?,
    @SerializedName("src", alternate = ["url"]) val src: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
): Parcelable
@Parcelize
data class AudioMessage(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("waveform") val waveform: List<Int>,
    @SerializedName("link_ogg") val linkOgg: String,
    @SerializedName("link_mp3") val linkMp3: String,
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("transcript_state") val transcriptState: String,
    @SerializedName("transcript") val transcript: String?
): Parcelable
@Parcelize
data class Preview(
    @SerializedName("photo") val photo: Photo?,
    @SerializedName("graffiti") val graffiti: Graffiti?,
    @SerializedName("audio_message") val audioMessage: AudioMessage?
): Parcelable
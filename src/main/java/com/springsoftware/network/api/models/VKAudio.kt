package com.springsoftware.network.api.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKAudio (
    val duration: Int = 0,
    val ownerId: Int = 0,
    val id: Int = 0,
    val accessKey: String = "",
    val artist: String = "",
    val title: String = "",
    var cover: String = "",
    val url: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(duration)
        parcel.writeInt(ownerId)
        parcel.writeInt(id)
        parcel.writeString(accessKey)
        parcel.writeString(artist)
        parcel.writeString(title)
        parcel.writeString(cover)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return id
    }

    companion object CREATOR : Parcelable.Creator<VKAudio> {
        override fun createFromParcel(parcel: Parcel): VKAudio {
            return VKAudio(parcel)
        }

        override fun newArray(size: Int): Array<VKAudio?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject) = VKAudio(
            id = json.optInt("id",0),
            ownerId = json.optInt("owner_id",0),
            accessKey = json.optString("access_key",""),
            duration = json.optInt("duration",0),
            artist = json.optString("artist",""),
            title = json.optString("title",""),
            url = json.optString("url",""),
            cover = ""
            //"https://raw.githubusercontent.com/JaneySprings/cappuccino-core/master/audio_api_unavailable.mp3" - memes
        )
    }
}
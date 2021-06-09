package com.springsoftware.network.api.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKPlaylist (
    val membersCount: Int = 0,
    val id: Int = 0,

    val title: String = "",
    val photo200: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),

        parcel.readString()!!,
        parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(membersCount)
        parcel.writeInt(id)

        parcel.writeString(title)
        parcel.writeString(photo200)
    }

    override fun describeContents(): Int {
        return id
    }

    companion object CREATOR : Parcelable.Creator<VKPlaylist> {
        private const val KEY_WORD = "Playlist#"

        override fun createFromParcel(parcel: Parcel): VKPlaylist {
            return VKPlaylist(parcel)
        }

        override fun newArray(size: Int): Array<VKPlaylist?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject) = VKPlaylist(
            id = json.getJSONObject("peer").optInt("id",0),
            membersCount = json.getJSONObject("chat_settings").optInt("members_count",0),

            title = json.getJSONObject("chat_settings")
                    .optString("title","").substringAfterLast(KEY_WORD),
            photo200 = if (json.getJSONObject("chat_settings").has("photo"))
                json.getJSONObject("chat_settings").getJSONObject("photo")
                        .optString("photo_200","") else ""
        )
    }
}
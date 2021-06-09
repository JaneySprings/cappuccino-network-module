package com.springsoftware.network.api.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKVideo (
    val duration: Int = 0,
    val ownerId: Int = 0,
    val views: Int = 0,
    val date: Int = 0,
    val id: Int = 0,

    val description: String = "",
    val accessKey: String = "",
    val preview: String = "",
    val player: String = "",
    val title: String = "",

    val canAdd: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),

        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,

        parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(duration)
        parcel.writeInt(ownerId)
        parcel.writeInt(views)
        parcel.writeInt(date)
        parcel.writeInt(id)

        parcel.writeString(description)
        parcel.writeString(accessKey)
        parcel.writeString(preview)
        parcel.writeString(player)
        parcel.writeString(title)

        parcel.writeByte(if (canAdd) 1 else 0)
    }

    override fun describeContents(): Int {
        return id
    }

    companion object CREATOR : Parcelable.Creator<VKVideo> {
        override fun createFromParcel(parcel: Parcel): VKVideo {
            return VKVideo(parcel)
        }

        override fun newArray(size: Int): Array<VKVideo?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject): VKVideo {
            val photo = if (json.has("image")) json.getJSONArray("image")
                    .optJSONObject(json.getJSONArray("image").length()/2)
                    .optString("url","") else ""

            return VKVideo(
                description = json.optString("description",""),
                accessKey = json.optString("access_key",""),
                canAdd = json.optInt("can_add",0) == 1,
                duration = json.optInt("duration",0),
                player = json.optString("player",""),
                ownerId = json.optInt("owner_id",0),
                title = json.optString("title",""),
                views = json.optInt("views",0),
                date = json.optInt("date",0),
                id = json.optInt("id",0),
                preview = photo)
        }
    }
}
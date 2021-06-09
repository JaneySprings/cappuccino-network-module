package com.springsoftware.network.api.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKPhoto (
    val ownerId: Int = 0,
    val likes: Int = 0,
    val date: Int = 0,
    val id: Int = 0,

    val accessKey: String = "",
    val size_m: String = "",
    val size_q: String = "",
    val size_y: String = "",
    val text: String = "",

    var userLikes: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
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
        parcel.writeInt(ownerId)
        parcel.writeInt(likes)
        parcel.writeInt(date)
        parcel.writeInt(id)

        parcel.writeString(accessKey)
        parcel.writeString(size_m)
        parcel.writeString(size_q)
        parcel.writeString(size_y)
        parcel.writeString(text)

        parcel.writeByte(if (userLikes) 1 else 0)
    }

    override fun describeContents(): Int {
        return id
    }

    companion object CREATOR : Parcelable.Creator<VKPhoto> {
        override fun createFromParcel(parcel: Parcel): VKPhoto {
            return VKPhoto(parcel)
        }

        override fun newArray(size: Int): Array<VKPhoto?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject): VKPhoto {
            var userLikes = false
            var likes = 0
            var sizeM = ""
            var sizeQ = ""
            var sizeY = ""

            if (json.has("sizes")) {
                val sizes = json.getJSONArray("sizes")

                for (i in 0 until sizes.length())
                    when (sizes.getJSONObject(i).optString("type", "")) {
                        "m" -> sizeM = sizes.getJSONObject(i).optString("url", "")
                        "q" -> sizeQ = sizes.getJSONObject(i).optString("url", "")
                        "y" -> sizeY = sizes.getJSONObject(i).optString("url", "")
                    }


                if (sizeM == "") sizeM = sizes.getJSONObject(1).optString("url", "")
                if (sizeQ == "") sizeQ = sizes.getJSONObject((sizes.length() - 1) / 2).optString("url", "")
                if (sizeY == "") sizeY = sizes.getJSONObject(sizes.length() - 1).optString("url", "")

            }
            if (json.has("likes")) {
                likes = json.getJSONObject("likes").optInt("count", 0)
                userLikes = json.getJSONObject("likes").optInt("user_likes", 0) == 1
            }

            return VKPhoto(
                accessKey = json.optString("access_key", ""),
                ownerId = json.optInt("owner_id", 0),
                text = json.optString("text", ""),
                date = json.optInt("date", 0),
                id = json.optInt("id", 0),
                userLikes = userLikes,
                size_m = sizeM,
                size_q = sizeQ,
                size_y = sizeY,
                likes = likes
            )
        }
    }
}
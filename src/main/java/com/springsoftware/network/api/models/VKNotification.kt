package com.springsoftware.network.api.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

data class VKNotification (
    var feedbackCount: Int = 0,
    val feedbackId: Int = 0,
    val fromId: Int = 0,
    val date: Int = 0,

    val feedbackText: String = "",
    val parentText: String = "",
    val firstName: String = "",
    val photo100: String = "",
    val fullName: String = "",
    val type: String = "",

    val newState: Boolean = false,

    val parentPhoto: ArrayList<VKPhoto> = arrayListOf(),
    val parentVideo: ArrayList<VKVideo> = arrayListOf()): Parcelable {

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
        parcel.readString()!!,

        parcel.readByte() != 0.toByte(),

        parcel.createTypedArrayList(VKPhoto)!!,
        parcel.createTypedArrayList(VKVideo)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(feedbackCount)
        parcel.writeInt(feedbackId)
        parcel.writeInt(fromId)
        parcel.writeInt(date)

        parcel.writeString(feedbackText)
        parcel.writeString(parentText)
        parcel.writeString(firstName)
        parcel.writeString(photo100)
        parcel.writeString(fullName)
        parcel.writeString(type)

        parcel.writeByte(if (newState) 1 else 0)

        parcel.writeTypedList(parentPhoto)
        parcel.writeTypedList(parentVideo)
    }

    override fun describeContents(): Int {
        return date
    }

    companion object CREATOR: Parcelable.Creator<VKNotification> {
        override fun createFromParcel(parcel: Parcel): VKNotification {
            return VKNotification(parcel)
        }

        override fun newArray(size: Int): Array<VKNotification?> {
            return arrayOfNulls(size)
        }

        fun parse(item: JSONObject, groups: JSONArray, profiles: JSONArray): VKNotification {
            val type = item.optString("type","")
            val date = item.optInt("date",0)
            val fromId =
                if (!item.getJSONObject("feedback").has("items"))
                    item.getJSONObject("feedback").optInt("from_id",0)
                else item.getJSONObject("feedback").getJSONArray("items")
                    .getJSONObject(0).optInt("from_id",0)

            var name = ""
            var fName = ""
            var photo = ""
            var pText = ""
            var fText = ""
            val pPhoto = arrayListOf<VKPhoto>()
            val pVideo = arrayListOf<VKVideo>()

            when (type) {
                "like_photo" -> pPhoto.add(VKPhoto.parse(item.getJSONObject("parent")))
                "like_video" -> pVideo.add(VKVideo.parse(item.getJSONObject("parent")))
                "like_post" -> pText = item.getJSONObject("parent").optString("text","")
                "like_comment" -> pText = item.getJSONObject("parent").optString("text","")
                "comment_post" -> {
                    pText = item.getJSONObject("parent").optString("text","")
                    fText = item.getJSONObject("feedback").optString("text","")
                }
                "comment_photo" -> {
                    pPhoto.add(VKPhoto.parse(item.getJSONObject("parent")))
                    pText = item.getJSONObject("feedback").optString("text","")
                }
                "comment_video" -> {
                    pVideo.add(VKVideo.parse(item.getJSONObject("parent")))
                    pText = item.getJSONObject("feedback").optString("text","")
                }
                "reply_comment" -> {
                    pText = item.getJSONObject("parent").optString("text","")
                    fText = item.getJSONObject("feedback").optString("text","")
                }
            }

            if (fromId < 0)
                for (i in 0 until groups.length()) {
                    if (fromId == -groups.optJSONObject(i).optInt("id",0)) {
                        fName = groups.optJSONObject(i).optString("name","")
                        photo = groups.optJSONObject(i).optString("photo_100","")
                        break
                    }
                }
            else
                for (i in 0 until profiles.length()) {
                    if (fromId == profiles.optJSONObject(i).optInt("id",0)) {
                        fName = profiles.optJSONObject(i).optString("first_name","") + " " +
                            profiles.optJSONObject(i).optString("last_name","")
                        name = profiles.optJSONObject(i).optString("first_name","")
                        photo = profiles.optJSONObject(i).optString("photo_100","")
                        break
                    }
                }

            return VKNotification(
                feedbackCount = item.getJSONObject("feedback").optInt("count",0),
                newState = ((System.currentTimeMillis() / 1000L).toInt() - date < 86400),
                feedbackText = fText,
                parentPhoto = pPhoto,
                parentVideo = pVideo,
                parentText = pText,
                feedbackId = fromId,
                fullName = fName,
                photo100 = photo,
                firstName = name,
                fromId = fromId,
                date = date,
                type = type)
        }
    }
}
package com.springsoftware.network.api.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKUser(
    val followers: Int = 0,
    val relation: Int = 0,
    val lastSeen: Int = 0,
    val friends: Int = 0,
    val albums: Int = 0,
    val videos: Int = 0,
    val id: Int = 0,

    val relationPartner: String = "",
    val screenName: String = "",
    val instagram: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val photoMax: String = "",
    val photo100: String = "",
    val photo200: String = "",
    val country: String = "",
    val status: String = "",
    val bDate: String = "",
    val about: String = "",
    val skype: String = "",
    val name: String = "",
    val site: String = "",

    val isBlockedByMe: Boolean = false,
    val isBlocked: Boolean = false,
    val isClosed: Boolean = false,
    val online: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
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
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,

            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(followers)
        parcel.writeInt(relation)
        parcel.writeInt(lastSeen)
        parcel.writeInt(friends)
        parcel.writeInt(albums)
        parcel.writeInt(videos)
        parcel.writeInt(id)

        parcel.writeString(relationPartner)
        parcel.writeString(screenName)
        parcel.writeString(instagram)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photoMax)
        parcel.writeString(photo100)
        parcel.writeString(photo200)
        parcel.writeString(country)
        parcel.writeString(status)
        parcel.writeString(bDate)
        parcel.writeString(about)
        parcel.writeString(skype)
        parcel.writeString(name)
        parcel.writeString(site)

        parcel.writeByte(if (isBlockedByMe) 1 else 0)
        parcel.writeByte(if (isBlocked) 1 else 0)
        parcel.writeByte(if (isClosed) 1 else 0)
        parcel.writeByte(if (online) 1 else 0)
    }

    override fun describeContents(): Int {
        return id
    }

    companion object CREATOR : Parcelable.Creator<VKUser> {
        override fun createFromParcel(parcel: Parcel): VKUser {
            return VKUser(parcel)
        }

        override fun newArray(size: Int): Array<VKUser?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject): VKUser {
            var partner = ""
            var country = ""
            var city = ""
            var followers = 0
            var friends = 0
            var albums = 0
            var videos = 0

            if (json.has("country")) {
                if (json.has("city"))
                    city = " · " + json.getJSONObject("city").optString("title","")
                country = json.getJSONObject("country").optString("title", "") + city
            }

            if (json.has("counters")) {
                followers = json.getJSONObject("counters").optInt("followers", 0)
                friends = json.getJSONObject("counters").optInt("friends", 0)
                albums = json.getJSONObject("counters").optInt("albums", 0)
                videos = json.getJSONObject("counters").optInt("videos", 0)
            }

            if (json.has("relation_partner"))
                partner = "· " + json.getJSONObject("relation_partner").optString("first_name","") + " " +
                        json.getJSONObject("relation_partner").optString("last_name","")

            return VKUser(
                name = json.optString("first_name", "Unknown") + " " + json.optString("last_name", "Unknown"),
                id = json.optInt("id", json.optInt("user_id", 0)),
                isBlockedByMe = json.optInt("blacklisted_by_me",0) == 1,
                lastSeen = if (json.has("last_seen"))
                    json.getJSONObject("last_seen").optInt("time") else 0,
                photoMax = json.optString("photo_max_orig", ""),
                screenName = json.optString("screen_name", ""),
                isBlocked = json.optInt("blacklisted",0) == 1,
                isClosed = json.optBoolean("is_closed",false),
                firstName = json.optString("first_name", ""),
                instagram = json.optString("instagram", ""),
                lastName = json.optString("last_name", ""),
                photo100 = json.optString("photo_100", ""),
                photo200 = json.optString("photo_200", ""),
                online = json.optInt("online",0) == 1,
                relation = json.optInt("relation", 0),
                status = json.optString("status", ""),
                bDate = json.optString("bdate", ""),
                about = json.optString("about", ""),
                skype = json.optString("skype", ""),
                site = json.optString("site", ""),

                relationPartner = partner,
                followers = followers,
                country = country,
                friends = friends,
                albums = albums,
                videos = videos
            )
        }
    }
}
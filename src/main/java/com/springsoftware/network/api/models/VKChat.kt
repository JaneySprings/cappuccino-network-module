package com.springsoftware.network.api.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import java.util.ArrayList

data class VKChat (
    val membersCount: Int = 0,
    val adminId: Int = 0,
    val id: Int = 0,

    val photo100: String = "",
    val title: String = "",
    val type: String = "",

    val users: ArrayList<VKUser> = arrayListOf()) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),

        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,

        parcel.createTypedArrayList(VKUser)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(membersCount)
        parcel.writeInt(adminId)
        parcel.writeInt(id)

        parcel.writeString(photo100)
        parcel.writeString(title)
        parcel.writeString(type)

        parcel.writeTypedList(users)
    }

    override fun describeContents(): Int {
        return id
    }

    companion object CREATOR : Parcelable.Creator<VKChat> {
        override fun createFromParcel(parcel: Parcel): VKChat {
            return VKChat(parcel)
        }

        override fun newArray(size: Int): Array<VKChat?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject): VKChat {
            val admin = json.optInt("admin_id",0)
            val users = arrayListOf<VKUser>()

            for(i in 0 until json.getJSONArray("users").length())
                users.add(VKUser.parse(json.getJSONArray("users").getJSONObject(i)))

            return VKChat(
                membersCount = json.optInt("members_count",0),
                photo100 = json.optString("photo_100",""),
                title = json.optString("title",""),
                type = json.optString("type",""),
                id = json.optInt("id",0),
                users = users,
                adminId = admin
            )
        }
    }
}
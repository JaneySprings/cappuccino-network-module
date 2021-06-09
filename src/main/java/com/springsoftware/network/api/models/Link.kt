package com.springsoftware.network.api.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/* Link model */
@Parcelize
data class Link(
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("caption") val caption: String?,
    @SerializedName("description") val description: String,
    @SerializedName("photo") val photo: Photo?
): Parcelable
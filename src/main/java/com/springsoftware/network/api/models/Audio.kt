package com.springsoftware.network.api.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/* Audio model */
@Parcelize
data class Audio(
    @SerializedName("artist") val artist: String,
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("is_explicit") val isExplicit: Boolean,
    @SerializedName("is_focus_track") val isFocusTrack: Boolean,
    @SerializedName("track_code") val trackCode: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("date") val date: Int,
    @SerializedName("no_search") val noSearch: Int,
    @SerializedName("main_artists") val mainArtists: List<Artist>?,
    @SerializedName("featured_artists") val featuredArtists: List<Artist>?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("access_key") val accessKey: String?,
    @SerializedName("album_id") val albumId: Int,
    @SerializedName("genre_id") val genreId: Int,
    @SerializedName("is_hq") val isHq: Boolean,
    var cover: String? = null
): Parcelable

@Parcelize
data class Artist(
        @SerializedName("name") val name: String,
        @SerializedName("domain") val domain: String,
        @SerializedName("id") val id: String
): Parcelable
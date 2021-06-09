package com.springsoftware.network.coverartarchive

import com.google.gson.annotations.SerializedName

data class CoverArtResponse(
    @SerializedName("images") val images: List<Image>
)
data class Image(
    @SerializedName("thumbnails") val thumbnails: Thumbnails
)
data class Thumbnails(
    @SerializedName("1200") val cover1200: String,
    @SerializedName("500") val cover500: String,
    @SerializedName("250") val cover250: String,
    @SerializedName("small") val small: String,
    @SerializedName("large") val large: String
)

data class MusicBrainzResponse(
    @SerializedName("created") val created: String,
    @SerializedName("count") val count: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("recordings") val recordings: List<Recording>?
)

data class Recording(
    @SerializedName("id") val id: String,
    @SerializedName("score") val score: Int,
    @SerializedName("title") val title: String,
    @SerializedName("length") val length: Int,
    @SerializedName("first-release-date") val firstReleaseDate: String,
    @SerializedName("releases") val releases: List<Release>,
    @SerializedName("artist-credit") val artistCredit: List<ArtistCredit>
)

data class ArtistCredit (
    @SerializedName("name") val name: String
)

data class Release(
    @SerializedName("id") val id: String,
    @SerializedName("status-id") val statusId: String,
    @SerializedName("count") val count: Int,
    @SerializedName("title") val title: String,
    @SerializedName("status") val status: String,
    @SerializedName("date") val date: String
)
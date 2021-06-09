package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("screen_name") val screenName: String,
    @SerializedName("is_closed") val isClosed: Int,
    @SerializedName("type") val type: String,
    @SerializedName("photo_50") val photo50: String,
    @SerializedName("photo_100") val photo100: String,
    @SerializedName("photo_200") val photo200: String
)

/* Extended */
data class GroupExtended(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("screen_name") val screenName: String,
    @SerializedName("is_closed") val isClosed: Int,
    @SerializedName("deactivated") val deactivated: String,
    @SerializedName("is_admin") val isAdmin: Int,
    @SerializedName("is_member") val isMember: Int,
    @SerializedName("type") val type: String,
    @SerializedName("photo_100") val photo100: String,
    @SerializedName("photo_50") val photo50: String,
    @SerializedName("photo_200") val photo200: String,
    @SerializedName("activity") val activity: String,
    @SerializedName("age_limits") val ageLimits: Int,
    @SerializedName("ban_info") val banInfo: BanInfo?,
    @SerializedName("can_message") val canMessage: Int,
    @SerializedName("city") val city: City?,
    @SerializedName("contacts") val contacts: List<Contact>,
    @SerializedName("country") val country: Country?,
    @SerializedName("cover") val cover: Cover?,
    @SerializedName("description") val description: String,
    @SerializedName("is_hidden_from_feed") val isHiddenFromFeed: Int,
    @SerializedName("member_status") val memberStatus: Int,
    @SerializedName("members_count") val membersCount: Int,
    @SerializedName("site") val site: String,
    @SerializedName("status") val status: String
)
data class BanInfo(
    @SerializedName("end_date") val endDate: Int,
    @SerializedName("comment") val comment: String
)
data class Contact(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("desc") val desc: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String
)
data class Cover(
    @SerializedName("enabled") val enabled: Int,
    @SerializedName("images") val images: List<Image>
)
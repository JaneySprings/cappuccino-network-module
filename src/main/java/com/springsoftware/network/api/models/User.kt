package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
        @SerializedName("items") val users: List<User>,
        @SerializedName("count") val count: Int
)

data class User(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("id") val id: Int,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("can_access_closed") val canAccessClosed: Boolean,
    @SerializedName("is_closed") val isClosed: Boolean,
    @SerializedName("sex") val sex: Int,
    @SerializedName("screen_name") val screenName: String,
    @SerializedName("photo_50") val photo50: String,
    @SerializedName("photo_100") val photo100: String,
    @SerializedName("last_seen") val lastSeen: LastSeen?,
    @SerializedName("city") val city: City?,
    @SerializedName("online") val online: Int
)

/* Extended */
data class UserExtended(
        @SerializedName("id") val id: Int,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("can_access_closed") val canAccessClosed: Boolean,
        @SerializedName("is_closed") val isClosed: Boolean,
        @SerializedName("about") val about: String,
        @SerializedName("activities") val activities: String,
        @SerializedName("bdate") val bDate: String,
        @SerializedName("blacklisted") val blacklisted: Int,
        @SerializedName("blacklisted_by_me") val blacklistedByMe: Int,
        @SerializedName("books") val books: String,
        @SerializedName("can_send_friend_request") val canSendFriendRequest: Int,
        @SerializedName("can_write_private_message") val canWritePrivateMessage: Int,
        @SerializedName("career") val career: List<Career>,
        @SerializedName("city") val city: City?,
        @SerializedName("skype") val skype: String?,
        @SerializedName("facebook") val facebook: String?,
        @SerializedName("twitter") val twitter: String?,
        @SerializedName("instagram") val instagram: String?,
        @SerializedName("mobile_phone") val mobilePhone: String,
        @SerializedName("home_phone") val homePhone: String,
        @SerializedName("counters") val counters: Counters?,
        @SerializedName("country") val country: Country?,
        @SerializedName("domain") val domain: String,
        @SerializedName("friend_status") val friendStatus: Int,
        @SerializedName("games") val games: String,
        @SerializedName("homeTown") val homeTown: String,
        @SerializedName("interests") val interests: String,
        @SerializedName("is_hidden_from_feed") val isHiddenFromFeed: Int,
        @SerializedName("last_seen") val lastSeen: LastSeen?,
        @SerializedName("maiden_name") val maidenName: String,
        @SerializedName("movies") val movies: String,
        @SerializedName("music ") val music : String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("occupation") val occupation: Occupation?,
        @SerializedName("online") val online: Int,
        @SerializedName("photo_50") val photo50: String,
        @SerializedName("photo_100") val photo100: String,
        @SerializedName("photo_200") val photo200: String,
        @SerializedName("photo_max_orig") val photoMaxOrig: String,
        @SerializedName("quotes") val quotes: String,
        @SerializedName("relation") val relation: Int,
        @SerializedName("relation_partner") val relationPartner: RelationPartner?,
        @SerializedName("schools") val schools: List<School>,
        @SerializedName("screen_name") val screenName: String,
        @SerializedName("site") val site: String,
        @SerializedName("status") val status: String?,
        @SerializedName("tv") val tv: String
)
data class Career(
    @SerializedName("company") val company: String,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("position") val position: String
)
data class Counters(
    @SerializedName("albums") val albums: Int,
    @SerializedName("videos") val videos: Int,
    @SerializedName("audios") val audios: Int,
    @SerializedName("photos") val photos: Int,
    @SerializedName("notes") val notes: Int,
    @SerializedName("friends") val friends: Int,
    @SerializedName("groups") val groups: Int,
    @SerializedName("online_friends") val onlineFriends: Int,
    @SerializedName("mutual_friends") val mutualFriends: Int,
    @SerializedName("user_videos") val userVideos: Int,
    @SerializedName("followers") val followers: Int,
    @SerializedName("pages") val pages: Int
)
data class LastSeen(
    @SerializedName("time") val time: Int,
    @SerializedName("platform") val platform: Int
)
data class Occupation(
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String
)
data class RelationPartner(
    @SerializedName("id") val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)
data class School(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int
)
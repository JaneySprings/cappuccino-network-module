package com.springsoftware.network.api.request

/**
 * Date: 03.02.2021
 * Author: Nikita Romanov
 * User fields [https://vk.com/dev/objects/user]
 */

object UserHelper {
    const val about = "about"
    const val activities = "activities"
    const val bDate = "bdate"
    const val blacklisted = "blacklisted"
    const val blacklistedByMe = "blacklisted_by_me"
    const val books = "books"
    const val canPost = "can_post"
    const val canSeeAllPosts = "can_see_all_posts"
    const val canSeeAudio = "can_see_audio"
    const val canSendFriendRequest = "can_send_friend_request"
    const val canWritePrivateMessage = "can_write_private_message"
    const val career = "career"
    const val city = "city"
    const val commonCount = "common_count"
    const val connections = "connections"
    const val contacts = "contacts"
    const val counters = "counters"
    const val country = "country"
    const val education = "education"
    const val exports = "exports"
    const val games = "games"
    const val homeTown = "home_town"
    const val interests = "interests"
    const val friendStatus = "friend_status"
    const val isFriend = "is_friend"
    const val isHiddenFromFeed = "is_hidden_from_feed"
    const val lastSeen = "last_seen"
    const val lists = "lists"
    const val maidenName = "maiden_name"
    const val movies = "movies"
    const val music = "music"
    const val nickname = "nickname"
    const val occupation = "occupation"
    const val online = "online"
    const val personal = "personal"
    const val photo50 = "photo_50"
    const val photo100 = "photo_100"
    const val photo200_orig = "photo_200_orig"
    const val photo200 = "photo_200"
    const val photo400_orig = "photo_400_orig"
    const val photoId = "photo_id"
    const val photoMax = "photo_max"
    const val photoMax_orig = "photo_max_orig"
    const val quotes = "quotes"
    const val relatives = "relatives"
    const val relation = "relation"
    const val schools = "schools"
    const val screenName = "screen_name"
    const val site = "site"
    const val status = "status"
    const val trending = "trending"
    const val tv = "tv"
    const val verified = "verified"

    fun defaultParamsList() = listOf(lastSeen, online, city, photo100).joinToString(",")

    fun extendedParamsList() = listOf(
        about, activities, bDate, blacklisted, blacklistedByMe, canSendFriendRequest,
        canWritePrivateMessage, city, connections, contacts, counters, country, friendStatus,
        lastSeen, online, photo100, photo200, photoMax_orig, screenName, site, status
    ).joinToString(",")
}
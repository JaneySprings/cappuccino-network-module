package com.springsoftware.network.api.request.friends

import com.google.gson.Gson
import com.springsoftware.network.api.models.UserResponse
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Date: 29.04.2021
 * Author: Nikita Romanov
 * Friends actions: [https://vk.com/dev/friends.search]
 */

class FriendsSearch(uid: Int, count: Int, q: String): VKRequest<UserResponse>("friends.search") {
    private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100)

    init {
        if (uid != 0) addParam("user_id", uid)
        addParam("q", q)
        addParam("fields", fields.joinToString(","))
        addParam("count", count)
    }

    override fun parse(r: JSONObject): UserResponse {
        val response = r.getJSONObject("response").toString()
        return Gson().fromJson(response, UserResponse::class.java)
    }
}
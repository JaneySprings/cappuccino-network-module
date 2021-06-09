package com.springsoftware.network.api.request.friends

import com.google.gson.Gson
import com.springsoftware.network.api.models.UserResponse
import com.vk.api.sdk.requests.VKRequest
import com.springsoftware.network.api.request.fields.UserHelper
import org.json.JSONObject

/**
 * Date: 29.04.2021
 * Author: Nikita Romanov
 * Friends actions: [https://vk.com/dev/friends.get]
 */

class FriendsGet(uid: Int, count: Int, order: String, offset: Int): VKRequest<UserResponse>("friends.get") {
    private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.city,
            UserHelper.photo100)

    init {
        addParam("user_id", uid)
        addParam("order", order)
        addParam("count", count)
        addParam("offset", offset)
        addParam("fields", fields.joinToString(","))
    }

    override fun parse(r: JSONObject): UserResponse {
        val response = r.getJSONObject("response").toString()
        return Gson().fromJson(response, UserResponse::class.java)
    }

    companion object {
        const val ORDER_NAME = "name"
        const val ORDER_HINTS = "hints"
        const val ORDER_RANDOM = "random"
    }
}
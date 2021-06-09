package com.springsoftware.network.api.request.friends

import com.vk.api.sdk.requests.VKRequest
import com.springsoftware.network.api.models.VKUser
import com.springsoftware.network.api.request.fields.UserHelper
import org.json.JSONObject
import java.util.ArrayList

/**
 * Date: 03.02.2021
 * Author: Nikita Romanov
 * Friends actions: [https://vk.com/dev/friends.getRequests]
 */

class FriendsGetRequests(out: Int, count: Int, offset: Int): VKRequest<List<VKUser>>("friends.getRequests") {
    private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100,
            UserHelper.status)

    init {
        addParam("offset", offset)
        addParam("count", count)
        addParam("extended",1)
        addParam("need_mutual",0)
        addParam("out", out)
        addParam("fields", fields.joinToString(","))
    }

    override fun parse(r: JSONObject): List<VKUser> {
        val users = r.getJSONObject("response").getJSONArray("items")
        val result = ArrayList<VKUser>()

        for (i in 0 until users.length())
            result.add(VKUser.parse(users.getJSONObject(i)))

        return result
    }
}
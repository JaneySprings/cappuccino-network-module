package com.springsoftware.network.api.request.messages

import com.springsoftware.network.api.models.VKUser
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getConversationMembers]
 */

class MessagesGetConversationMembers(id: Int): VKRequest<List<VKUser>>("messages.getConversationMembers") {
    private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100,
            UserHelper.status)

    init {
        addParam("peer_id", id)
        addParam("fields", fields)
    }

    override fun parse(r: JSONObject): List<VKUser> {
        val items = r.getJSONObject("response").getJSONArray("profiles")
        val result = ArrayList<VKUser>()

        for (i in 0 until items.length())
            result.add(VKUser.parse(items.getJSONObject(i)))

        return result
    }
}
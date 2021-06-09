package com.springsoftware.network.api.request.messages

import com.springsoftware.network.api.models.VKChat
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getChat]
 */

class MessagesGetChat(id: Int): VKRequest<VKChat>("messages.getChat") {
    private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100)

    init {
        addParam("chat_id", id)
        addParam("fields", fields.joinToString(","))
    }

    override fun parse(r: JSONObject) = VKChat.parse(r.getJSONObject("response"))
}
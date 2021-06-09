package com.springsoftware.network.api.request.messages

import com.google.gson.Gson
import com.springsoftware.network.api.mappers.toConversationItems
import com.springsoftware.network.api.models.ConversationItem
import com.springsoftware.network.api.models.ConversationsDTO
import com.springsoftware.network.api.models.ConversationsResponse
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Date: 02.05.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getConversations]
 */

class MessagesGetConversations(count: Int, offset: Int): VKRequest<ConversationsDTO>("messages.getConversations") {
    private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100)

    init {
        addParam("offset", offset)
        addParam("count", count)
        addParam("filter","all")
        addParam("extended", 1)
        addParam("fields", fields.joinToString(","))
    }

    override fun parse(r: JSONObject): ConversationsDTO {
        val response = r.getJSONObject("response").toString()
        val dto = Gson().fromJson(response, ConversationsResponse::class.java)

        return ConversationsDTO(dto.toConversationItems(), dto.count)
    }
}
package com.springsoftware.network.api.request.messages

import com.google.gson.Gson
import com.springsoftware.network.api.mappers.toConversationItems
import com.springsoftware.network.api.models.ConversationsByIdResponse
import com.springsoftware.network.api.models.ConversationsDTO
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Date: 04.05.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getConversationsById]
 */

class MessagesGetConversationsById(ids: List<Int>): VKRequest<ConversationsDTO>("messages.getConversationsById") {
    private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100)

    init {
        addParam("peer_ids", ids.joinToString(","))
        addParam("extended", 1)
        addParam("fields", fields.joinToString(","))
    }

    override fun parse(r: JSONObject): ConversationsDTO {
        val response = r.getJSONObject("response").toString()
        val dto = Gson().fromJson(response, ConversationsByIdResponse::class.java)

        return ConversationsDTO(dto.toConversationItems(), dto.count)
    }
}
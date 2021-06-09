package com.springsoftware.network.api.request.messages

import com.google.gson.Gson
import com.springsoftware.network.api.models.MessageEntity
import com.springsoftware.network.api.models.MessageItem
import com.springsoftware.network.api.models.MessageResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

/**
 * Date: 21.04.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getHistory]
 */

class MessagesGetHistory(uid: Int, count: Int, offset: Int): VKRequest<MessageEntity>("messages.getHistory") {
    init {
        addParam("offset", offset)
        addParam("count", count)
        addParam("peer_id", uid)
        addParam("rev",0)
        addParam("extended",1)
    }

    override fun parse(r: JSONObject): MessageEntity {
        val response = r.getJSONObject("response").toString()
        val dto = Gson().fromJson(response, MessageResponse::class.java)
        val messages = ArrayList<MessageItem>()

        for (i in dto.items.indices)
            messages.add(MessageItem.fromResponse(dto.items[i], dto.profiles, dto.groups))


        return MessageEntity(messages, dto.count)
    }
}
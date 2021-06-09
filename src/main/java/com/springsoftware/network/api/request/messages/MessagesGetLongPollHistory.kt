package com.springsoftware.network.api.request.messages

import com.google.gson.Gson
import com.springsoftware.network.api.models.LongPollEntity
import com.springsoftware.network.api.models.LongPollResponse
import com.springsoftware.network.api.models.MessageItem
import com.springsoftware.network.api.request.fields.UserHelper
import com.springsoftware.network.api.request.messages.MessagesGetLongPollServer.Companion.LONG_POOL_VERSION
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

/**
 * Date: 10.03.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getLongPollHistory]
 */

class MessagesGetLongPollHistory(keys: Array<Int>): VKRequest<LongPollEntity>("messages.getLongPollHistory") {
    init {
        addParam("ts", keys[0])
        addParam("pts", keys[1])
        addParam("fields", UserHelper.photo100)
        addParam("lp_version", LONG_POOL_VERSION)
    }

    override fun parse(r: JSONObject): LongPollEntity {
        val response = r.getJSONObject("response").toString()
        val dto = Gson().fromJson(response, LongPollResponse::class.java)
        val messages = ArrayList<MessageItem>()

        for (i in dto.messages.items.indices)
            messages.add(MessageItem.fromResponse(dto.messages.items[i], dto.profiles, dto.groups))

        return LongPollEntity(dto.history, messages, dto.newPts)
    }

    companion object {
        private const val ITEM_ID_INDEX = 1
    }
}
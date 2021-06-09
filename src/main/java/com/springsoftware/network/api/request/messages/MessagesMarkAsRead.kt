package com.springsoftware.network.api.request.messages

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.markAsRead]
 */

class MessagesMarkAsRead(private val peerId: Int): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("messages.markAsRead")
            .args("peer_id", peerId)
            .args("mark_conversation_as_read", 1)
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser: VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response).getInt("response")
    }
}
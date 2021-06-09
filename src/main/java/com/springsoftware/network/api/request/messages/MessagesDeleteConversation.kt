package com.springsoftware.network.api.request.messages

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.deleteConversation]
 */

class MessagesDeleteConversation (private val id: Int): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder().apply {
            method("messages.deleteConversation")

            if (id in 0 until CHAT_ID_DELTA) args("user_id", id)
            else args("peer_id", id)

            version(manager.config.version)
        }

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser: VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response)
                .getJSONObject("response").optInt("last_deleted_id",0)
    }

    companion object {
        const val CHAT_ID_DELTA = 2000000000
    }
}
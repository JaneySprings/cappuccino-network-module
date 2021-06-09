package com.springsoftware.network.api.request.messages

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.delete]
 */

class MessagesDelete (private val ids: List<Int>, private val forAll: Int): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("messages.delete")
            .args("message_ids", ids.joinToString(","))
            .args("spam", 0)
            .args("delete_for_all", forAll)
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser: VKApiResponseParser<Int> {
        override fun parse(response: String) =
                if (JSONObject(response).has("response")) 1 else 0
    }
}
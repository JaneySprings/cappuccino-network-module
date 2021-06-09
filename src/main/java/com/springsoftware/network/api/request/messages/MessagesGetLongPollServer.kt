package com.springsoftware.network.api.request.messages

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getLongPollServer]
 */

class MessagesGetLongPollServer: ApiCommand<List<Int>>() {
    override fun onExecute(manager: VKApiManager): List<Int> {
        val callBuilder = VKMethodCall.Builder()
            .method("messages.getLongPollServer")
            .args("need_pts", 1)
            .args("lp_version", LONG_POOL_VERSION)
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser: VKApiResponseParser<List<Int>> {
        override fun parse(response: String): List<Int> {
            val keys = JSONObject(response).getJSONObject("response")
            return listOf(keys.getInt("ts"), keys.getInt("pts"))
        }
    }

    companion object {
        const val LONG_POOL_VERSION = 3
    }
}


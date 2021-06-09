package com.springsoftware.network.api.request.newsfeed

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 25.04.2021
 * Author: Nikita Romanov
 * Document actions: [https://vk.com/dev/newsfeed.ignoreItem]
 */

class NewsfeedIgnoreItem(private val type: String, private val ownerId: Int, private val id: Int): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("newsfeed.ignoreItem")
            .args("type", type)
            .args("owner_id", ownerId)
            .args("item_id", id)
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response).getInt("response")
    }
}
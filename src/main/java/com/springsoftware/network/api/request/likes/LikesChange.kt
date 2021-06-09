package com.springsoftware.network.api.request.likes

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 03.02.2021
 * Author: Nikita Romanov
 * Likes actions: [https://vk.com/dev/likes.delete]
 * [https://vk.com/dev/likes.add]
 */

class LikesChange(
        private val type: String,
        private val ownerId: Int,
        private val sourceId: Int,
        private val flag: Boolean): ApiCommand<Int>() {

    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method(if (flag) METHOD_DELETE else METHOD_ADD)
            .args("type", type)
            .args("owner_id", ownerId)
            .args("item_id", sourceId)
            .version(manager.config.version)
        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response)
                .getJSONObject("response").getInt("likes")
    }

    companion object {
        const val METHOD_ADD = "likes.add"
        const val METHOD_DELETE = "likes.delete"
    }
}
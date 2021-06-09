package com.springsoftware.network.api.request.friends

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 03.02.2021
 * Author: Nikita Romanov
 * Friends actions: [https://vk.com/dev/friends.add]
 * [https://vk.com/dev/friends.delete]
 */

class FriendsChangeState (private val ownerId: Int, private val state: Int): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
                .method(
                        if (state == STATE_NOT_FRIEND || state == STATE_IS_SUBSCRIBED) METHOD_ADD
                        else METHOD_DELETE)
                .args("user_id", ownerId)
                .version(manager.config.version)

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser: VKApiResponseParser<Int> {
        override fun parse(response: String): Int {
            return if (JSONObject(response).has("response")) 1 else 0
        }
    }

    companion object {
        private const val METHOD_ADD = "friends.add"
        private const val METHOD_DELETE = "friends.delete"

        private const val STATE_NOT_FRIEND = 0
        private const val STATE_IS_SUBSCRIBED = 2
    }
}
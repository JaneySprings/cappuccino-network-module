package com.springsoftware.network.api.request.account

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 02.02.2021
 * Author: Nikita Romanov
 * Account actions: [https://vk.com/dev/account.ban]
 * [https://vk.com/dev/account.unban]
 */

class AccountBanAction (private val ownerId: Int, private val flag: Boolean): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method(if (flag) METHOD_UNBAN else METHOD_BAN)
            .args("owner_id", ownerId)
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response).getInt("response")
    }

    companion object {
        const val METHOD_BAN = "account.ban"
        const val METHOD_UNBAN = "account.unban"
    }
}
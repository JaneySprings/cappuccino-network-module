package com.springsoftware.network.api.request.account

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 04.04.2021
 * Author: Nikita Romanov
 * Account actions: [https://vk.com/dev/account.setSilenceMode]
 */

class AccountSetSilenceMode(private val id: Int, private val state: Boolean): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("account.setSilenceMode")
            .args("device_id", manager.config.deviceId)
            .args("time", if (state) -1 else 10)
            .args("peer_id", id)
            .args("sound", if (state) 0 else 1)
            .version(manager.config.version)
        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response).getInt("response")
    }
}
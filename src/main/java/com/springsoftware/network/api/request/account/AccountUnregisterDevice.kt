package com.springsoftware.network.api.request.account

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 02.02.2021
 * Author: Nikita Romanov
 * Account actions: [https://vk.com/dev/account.unregisterDevice]
 */

class AccountUnregisterDevice: ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("account.unregisterDevice")
            .args("device_id", manager.config.deviceId)
            .version(manager.config.version)
        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response).getInt("response")
    }
}
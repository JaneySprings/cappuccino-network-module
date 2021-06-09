package com.springsoftware.network.api.request.account

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 02.02.2021
 * Author: Nikita Romanov
 * Account actions: [https://vk.com/dev/account.registerDevice]
 */

class AccountRegisterDevice(private val token: String): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("account.registerDevice")
            .args("token", token)
            .args("device_id", manager.config.deviceId)
            .args("settings", PUSH_SETTING)
            .version(manager.config.version)
        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response).getInt("response")
    }

    companion object {
        const val PUSH_SETTING = "{\"msg\":[\"on\"],\"chat\":[\"no_sound\"],\"friend\":[\"on\"]}"
    }
}
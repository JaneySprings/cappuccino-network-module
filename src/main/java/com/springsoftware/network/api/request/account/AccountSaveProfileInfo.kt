package com.springsoftware.network.api.request.account

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

/**
 * Date: 03.04.2021
 * Author: Nikita Romanov
 * Account actions: [https://vk.com/dev/account.saveProfileInfo]
 */

class AccountSaveProfileInfo(
    private val screenName: String,
    private val firstName: String,
    private val lastName: String,
    private val status: String,
    private val bDate: String,
    private val relation: Int = -1): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
                .method("account.saveProfileInfo")
                .version(manager.config.version)

        if (firstName.isNotEmpty()) callBuilder.args("first_name", firstName)
        if (lastName.isNotEmpty()) callBuilder.args("last_name", lastName)
        if (screenName.isNotEmpty()) callBuilder.args("screen_name", screenName)
        if (relation != -1) callBuilder.args("relation", relation)
        if (bDate.isNotEmpty()) callBuilder.args("bdate", bDate)
        if (status.isNotEmpty()) callBuilder.args("status", status)

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String) = JSONObject(response)
                .getJSONObject("response").getInt("changed")
    }
}
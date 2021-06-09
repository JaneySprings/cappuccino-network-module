package com.springsoftware.network.api.request.account

import com.google.gson.Gson
import com.springsoftware.network.api.models.Counter
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Date: 29.05.2021
 * Author: Nikita Romanov
 * Account actions: [https://vk.com/dev/account.getCounters]
 */

class AccountGetCounters: VKRequest<Counter>("account.getCounters") {
    override fun parse(r: JSONObject): Counter {
        val response = r.getJSONObject("response").toString()
        return Gson().fromJson(response, Counter::class.java)
    }
}
package com.springsoftware.network.api.request

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONArray
import org.json.JSONObject

object NotificationsApi {
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * Notification actions: [https://vk.com/dev/notifications.get]
     */
    class Get(filter: String): VKRequest<List<Int>>("notifications.get") {
        init {
            addParam("count", MAX_ACTIONS_COUNT)
            addParam("filters", filter)
        }

        override fun parse(r: JSONObject): List<Int> {
            val json = r.getJSONObject("response")
            val items = json.getJSONArray("items")
            val groups = json.optJSONArray("groups") ?: JSONArray()
            val profiles = json.optJSONArray("profiles") ?: JSONArray()
            val data = arrayListOf<Int>()

            for (i in 0 until items.length())
                data.add(21)

            return data
        }

        companion object {
            const val MAX_ACTIONS_COUNT = 20
        }
    }
}
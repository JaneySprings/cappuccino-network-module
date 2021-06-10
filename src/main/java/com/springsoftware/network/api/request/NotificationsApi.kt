package com.springsoftware.network.api.request

import com.springsoftware.network.api.models.VKNotification
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONArray
import org.json.JSONObject

object NotificationsApi {
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * Notification actions: [https://vk.com/dev/notifications.get]
     */
    class Get(filter: String): VKRequest<List<VKNotification>>("notifications.get") {
        init {
            addParam("count", MAX_ACTIONS_COUNT)
            addParam("filters", filter)
        }

        override fun parse(r: JSONObject): List<VKNotification> {
            val json = r.getJSONObject("response")
            val items = json.getJSONArray("items")
            val groups = json.optJSONArray("groups") ?: JSONArray()
            val profiles = json.optJSONArray("profiles") ?: JSONArray()
            val data = arrayListOf<VKNotification>()

            for (i in 0 until items.length())
                data.add(VKNotification.parse(items.getJSONObject(i), groups, profiles))

            return data
        }

        companion object {
            const val MAX_ACTIONS_COUNT = 20
        }
    }
}
package com.springsoftware.network.api.request

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

object LikesApi {
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * Likes actions: [https://vk.com/dev/likes.delete]
     * [https://vk.com/dev/likes.add]
     */
    class Change(
        type: String,
        ownerId: Int,
        sourceId: Int,
        isLiked: Boolean
    ): VKRequest<Int>(if (isLiked) "likes.delete" else "likes.add") {
        init {
            addParam("type", type)
            addParam("owner_id", ownerId)
            addParam("item_id", sourceId)
        }
        override fun parse(r: JSONObject) = r.getJSONObject("response").getInt("likes")
    }
}
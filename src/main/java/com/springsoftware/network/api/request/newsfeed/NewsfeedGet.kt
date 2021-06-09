package com.springsoftware.network.api.request.newsfeed

import com.google.gson.Gson
import com.springsoftware.network.api.models.NewsfeedEntity
import com.springsoftware.network.api.models.PostItem
import com.springsoftware.network.api.models.PostResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Date: 23.04.2021
 * Author: Nikita Romanov
 * News actions: [https://vk.com/dev/newsfeed.get]
 */

class NewsfeedGet(count: Int, shift: String): VKRequest<NewsfeedEntity>("newsfeed.get") {
    init {
        addParam("filters","post")
        addParam("start_from", shift)
        addParam("count", count)
    }

    override fun parse(r: JSONObject): NewsfeedEntity {
        val response = r.getJSONObject("response").toString()
        val dto = Gson().fromJson(response, PostResponse::class.java)
        val items = arrayListOf<PostItem>()

        for (i in dto.items.indices)
            items.add(PostItem.fromResponse(dto.items[i], dto.profiles, dto.groups))

        return NewsfeedEntity(items, dto.nextFrom)
    }
}
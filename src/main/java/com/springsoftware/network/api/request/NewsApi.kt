package com.springsoftware.network.api.request

import com.google.gson.Gson
import com.springsoftware.network.api.models.NewsfeedEntity
import com.springsoftware.network.api.models.PostItem
import com.springsoftware.network.api.models.PostResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

object NewsApi {
    /**
     * Date: 23.04.2021
     * Author: Nikita Romanov
     * News actions: [https://vk.com/dev/newsfeed.get]
     */
    class Get(count: Int, shift: String): VKRequest<NewsfeedEntity>("newsfeed.get") {
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
    /**
     * Date: 17.04.2021
     * Author: Nikita Romanov
     * News actions: [https://vk.com/dev/newsfeed.getRecommended]
     */
    class GetRecommended(count: Int, shift: String): VKRequest<NewsfeedEntity>("newsfeed.getRecommended") {
        init {
            if (shift.isNotEmpty()) addParam("start_from", shift)
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
    /**
     * Date: 25.04.2021
     * Author: Nikita Romanov
     * Document actions: [https://vk.com/dev/newsfeed.ignoreItem]
     */
    class IgnoreItem(type: String, ownerId: Int, id: Int): VKRequest<Int>("newsfeed.ignoreItem") {
        init {
            addParam("type", type)
            addParam("owner_id", ownerId)
            addParam("item_id", id)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 17.04.2021
     * Author: Nikita Romanov
     * News actions: [https://vk.com/dev/newsfeed.search]
     */
    class Search(query: String, count: Int, shift: String): VKRequest<NewsfeedEntity>("newsfeed.search") {
        init {
            addParam("q", query)
            addParam("start_from", shift)
            addParam("count", count)
            addParam("extended",1)
        }

        override fun parse(r: JSONObject): NewsfeedEntity {
            val response = r.getJSONObject("response").toString()
            val dto = Gson().fromJson(response, PostResponse::class.java)
            val items = arrayListOf<PostItem>()

            for (i in dto.items.indices)
                if (dto.items[i].text.split("#").size <= FLOOD_FILTER_TAG_MAX)
                    items.add(PostItem.fromResponse(dto.items[i], dto.profiles, dto.groups))

            return NewsfeedEntity(items, dto.nextFrom)
        }

        companion object {
            private const val FLOOD_FILTER_TAG_MAX = 10
        }
    }
}
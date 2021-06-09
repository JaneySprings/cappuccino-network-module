package com.springsoftware.network.api.request

import com.google.gson.Gson
import com.springsoftware.network.api.models.DocsResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

object DocsApi {
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * Document actions: [https://vk.com/dev/docs.add]
     */
    class Add(ownerId: Int, id: Int, key: String): VKRequest<Int>("docs.add") {
        init {
            addParam("owner_id", ownerId)
            addParam("doc_id", id)
            addParam("access_key", key)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * Document actions: [https://vk.com/dev/docs.get]
     */
    class Get(count: Int, offset: Int, type: Int): VKRequest<DocsResponse>("docs.get") {
        init {
            addParam("count", count)
            addParam("offset", offset)
            addParam("type", type)
        }

        override fun parse(r: JSONObject): DocsResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, DocsResponse::class.java)
        }
    }
    /**
     * Date: 09.04.2021
     * Author: Nikita Romanov
     * Document actions: [https://vk.com/dev/docs.getById]
     */
    class GetById(meta: List<String>): VKRequest<DocsResponse>("docs.getById") {
        init { addParam("docs", meta.joinToString(",")) }

        override fun parse(r: JSONObject): DocsResponse {
            val response = r.getJSONArray("response").toString()
            return Gson().fromJson(response, DocsResponse::class.java)
        }
    }
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * Document actions: [https://vk.com/dev/docs.search]
     */
    class Search(q: String, count: Int, offset: Int): VKRequest<DocsResponse>("docs.search") {
        init {
            addParam("q", q)
            addParam("count", count)
            addParam("offset", offset)
        }

        override fun parse(r: JSONObject): DocsResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, DocsResponse::class.java)
        }
    }
}
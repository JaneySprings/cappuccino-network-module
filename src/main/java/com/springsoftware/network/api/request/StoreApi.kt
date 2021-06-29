package com.springsoftware.network.api.request

import com.google.gson.Gson
import com.springsoftware.network.api.models.StickersResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

object StoreApi {
    /**
     * Date: 27.06.2021
     * Author: Nikita Romanov
     * Document actions: [https://vk.com/dev/store.getProducts]
     */
    class GetProducts(filter: String = "purchased"): VKRequest<StickersResponse>("store.getProducts") {
        init {
            addParam("type", "stickers")
            addParam("filters", filter)
            addParam("extended", 1)
        }
        override fun parse(r: JSONObject): StickersResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, StickersResponse::class.java)
        }
    }
}
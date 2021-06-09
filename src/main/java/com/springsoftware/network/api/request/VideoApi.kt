package com.springsoftware.network.api.request

import com.google.gson.Gson
import com.springsoftware.network.api.models.VideoResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

object VideoApi {
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * Videos actions: [https://vk.com/dev/video.add]
     */
    class Add(vid: Int, oid: Int): VKRequest<Int>("video.add") {
        init {
            addParam("video_id", vid)
            addParam("owner_id", oid)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * Videos actions: [https://vk.com/dev/video.get]
     */
    class Get(uid: Int, count: Int, offset: Int): VKRequest<VideoResponse>("video.get") {
        init {
            addParam("owner_id", uid)
            addParam("count", count)
            addParam("offset", offset)
        }

        override fun parse(r: JSONObject): VideoResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, VideoResponse::class.java)
        }
    }
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * Videos actions: [https://vk.com/dev/video.get]
     */
    class GetById(ownerId: Int, vid: Int, key: String): VKRequest<VideoResponse>("video.get") {
        init { addParam("videos", "${ownerId}_${vid}_$key") }

        override fun parse(r: JSONObject): VideoResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, VideoResponse::class.java)
        }
    }
    /**
     * Date: 11.04.2021
     * Author: Nikita Romanov
     * Videos actions: [https://vk.com/dev/video.search]
     */
    class Search(q: String, count: Int, sort: Int, adult: Int, offset: Int): VKRequest<VideoResponse>("video.search") {
        init {
            addParam("q", q)
            addParam("sort", sort)
            addParam("adult", adult)
            addParam("offset", offset)
            addParam("count", count)
        }

        override fun parse(r: JSONObject): VideoResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, VideoResponse::class.java)
        }

        companion object {
            const val VIDEO_LONG = 1
            const val VIDEO_DEFAULT = 0

            const val SORT_RELEVANT = 2
            const val SORT_BY_DATE = 1

            const val SEARCH_SAFE = 0
            const val SEARCH_ADULT = 1
        }
    }
}
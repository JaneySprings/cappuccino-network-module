package com.springsoftware.network.api.request.messages

import com.google.gson.Gson
import com.springsoftware.network.api.models.*
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Date: 23.05.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.getHistoryAttachments]
 */

class MessagesGetHistoryAttachments(uid: Int, count: Int, shift: String, type: String): VKRequest<AttachmentResponse>("messages.getHistoryAttachments") {
    init {
        addParam("peer_id", uid)
        addParam("media_type", type)
        addParam("start_from", shift)
        addParam("count", count)
    }

    override fun parse(r: JSONObject): AttachmentResponse {
        val response = r.getJSONObject("response").toString()
        return Gson().fromJson(response, AttachmentResponse::class.java)
    }

    companion object {
        const val TYPE_PHOTO = "photo"
        const val TYPE_VIDEO = "video"
        const val TYPE_AUDIO = "audio"
        const val TYPE_DOC = "doc"
        const val TYPE_LINK = "link"
        const val TYPE_MARKET = "market"
        const val TYPE_WALL = "wall"
        const val TYPE_SHARE = "share"
    }
}
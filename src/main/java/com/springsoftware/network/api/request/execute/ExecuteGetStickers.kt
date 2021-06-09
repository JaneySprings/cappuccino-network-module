package com.springsoftware.network.api.request.execute

import com.springsoftware.network.api.models.VKSticker
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

/**
 * CUSTOM METHOD!
 * Date: 9.04.2021
 * Author: Nikita Romanov
 * Stickers action: stickers.get
 */

class ExecuteGetStickers(meta: String): VKRequest<List<VKSticker>>("execute.getStickers") {
    init { addParam("meta", meta) }

    override fun parse(r: JSONObject): List<VKSticker> {
        val response = r.getJSONArray("response")
        val result = ArrayList<VKSticker>()

        for (i in 0 until response.length())
            result.add(VKSticker.parse(response.getJSONObject(i)))

        return result
    }
}
package com.springsoftware.network.api.request.execute

import com.springsoftware.network.api.models.VKPlaylist
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

/**
 * CUSTOM METHOD!
 * Date: 17.02.2021
 * Author: Nikita Romanov
 * Playlist actions: playlist.Get
 */

class ExecuteGetPlaylists: VKRequest<List<VKPlaylist>>("execute.getPlaylists") {
    override fun parse(r: JSONObject): List<VKPlaylist> {
        val items = r.getJSONObject("response").getJSONArray("items")
        val result = ArrayList<VKPlaylist>()

        for (i in 0 until items.length())
            result.add(VKPlaylist.parse(items.getJSONObject(i)))

        return result
    }
}
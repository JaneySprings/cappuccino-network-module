package com.springsoftware.network.api.models

import org.json.JSONObject

data class VKSticker(
    val ownerId: Int = 0,
    val id: Int = 0,
    val preview: String = "") {

    companion object {
        fun parse(json: JSONObject): VKSticker {
            val photo =
                    if (json.has("preview")) json.getJSONObject("preview").getJSONObject("graffiti")
                            .optString("src","")
                    else ""

            return VKSticker(
                    ownerId = json.optInt("owner_id",0),
                    preview = photo,
                    id = json.optInt("id",0)
            )
        }
    }
}
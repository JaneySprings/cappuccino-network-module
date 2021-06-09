package com.springsoftware.network.api.request.newsfeed

import com.google.gson.Gson
import com.springsoftware.network.api.models.NewsfeedEntity
import com.springsoftware.network.api.models.PostItem
import com.springsoftware.network.api.models.PostResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import kotlin.random.Random

/**
 * Date: 17.04.2021
 * Author: Nikita Romanov
 * News actions: [https://vk.com/dev/newsfeed.search]
 */

class NewsfeedSearch(tagId: Int, count: Int, shift: String): VKRequest<NewsfeedEntity>("newsfeed.search") {
    init {
        val index = Random.nextInt(6)

        addParam("q", when(tagId) {
            ID_ART -> TAG_ART[index]
            ID_DEV -> TAG_DEV[index]
            ID_SPACE -> TAG_SPACE[index]
            ID_SCIENCE -> TAG_SCIENCE[index]
            ID_TOURISM -> TAG_TOURISM[index]
            ID_MATH -> TAG_MATH[index]
            else -> ""
        })
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
        private const val ID_ART = 2
        private const val ID_SPACE = 3
        private const val ID_SCIENCE = 4
        private const val ID_MATH = 5
        private const val ID_DEV = 6
        private const val ID_TOURISM = 7

        private val TAG_ART = arrayOf("#art", "#paint","#painting","#арт","#sketch","#illustration")
        private val TAG_DEV = arrayOf("#it", "#программирование", "#programming", "#developer", "#algorithm", "#dev")
        private val TAG_SPACE = arrayOf("#NASA", "#solarsystem", "#astronomy", "#astrophotography", "#astrophoto", "#хаббл")
        private val TAG_SCIENCE = arrayOf("#наука", "#science", "#черниговская", "#инновации", "#enginetica", "#technology")
        private val TAG_MATH = arrayOf("#math", "#math", "#алгебра", "#math", "#физика", "#physics")
        private val TAG_TOURISM = arrayOf("#journey", "#travel", "#земля", "#nature", "#atmosphere", "#путешествия")

        private const val FLOOD_FILTER_TAG_MAX = 10
    }
}
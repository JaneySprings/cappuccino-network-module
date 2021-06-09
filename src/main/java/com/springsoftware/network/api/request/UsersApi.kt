package com.springsoftware.network.api.request

import com.google.gson.Gson
import com.springsoftware.network.api.models.UserExtended
import com.springsoftware.network.api.models.UserResponse
import com.springsoftware.network.api.models.VKUser
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

object UsersApi {
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * User actions: [https://vk.com/dev/users.get]
     */
    class Get(ids: List<Int>): VKRequest<List<UserExtended>>("users.get") {
        private val fields = listOf(
            UserHelper.about, UserHelper.activities, UserHelper.bDate, UserHelper.blacklisted,
            UserHelper.blacklistedByMe, UserHelper.canSendFriendRequest,
            UserHelper.canWritePrivateMessage, UserHelper.city, UserHelper.connections,
            UserHelper.contacts, UserHelper.counters, UserHelper.commonCount, UserHelper.country,
            UserHelper.friendStatus, UserHelper.lastSeen, UserHelper.online, UserHelper.photo100,
            UserHelper.photo200, UserHelper.photoMax_orig, UserHelper.screenName, UserHelper.site,
            UserHelper.status)

        init {
            if (ids.isNotEmpty())
                addParam("user_ids", ids.chunked(USER_LIMIT)[0].joinToString(","))
            addParam("fields", fields.joinToString(","))
        }

        override fun parse(r: JSONObject): List<UserExtended> {
            val users = r.getJSONArray("response")
            val result = ArrayList<UserExtended>()

            for (i in 0 until users.length())
                result.add(Gson().fromJson(users.getJSONObject(i).toString(),
                    UserExtended::class.java))

            return result
        }

        companion object {
            private const val USER_LIMIT = 1000
        }
    }
    /**
     * Date: 15.05.2021
     * Author: Nikita Romanov
     * User actions: [https://vk.com/dev/users.getFollowers]
     */
    class GetFollowers(uid: Int, count: Int, offset: Int): VKRequest<UserResponse>("users.getFollowers") {
        private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.city,
            UserHelper.photo100)

        init {
            addParam("user_id", uid)
            addParam("offset", offset)
            addParam("count", count)
            addParam("fields", fields)
        }

        override fun parse(r: JSONObject): UserResponse {
            val response = r.getJSONObject("response").toString()

            return Gson().fromJson(response, UserResponse::class.java)
        }
    }
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * User actions: [https://vk.com/dev/users.report]
     */
    class Report(uid: Int, type: Int): VKRequest<Int>("users.report") {
        private val cause = when(type) {
            REQUEST_PORN -> TYPE_PORN
            REQUEST_INSULT -> TYPE_INSULT
            REQUEST_ADVERTISEMENT -> TYPE_ADVERTISEMENT
            else -> TYPE_SPAM
        }

        init {
            addParam("user_id", uid)
            addParam("type", cause)
        }

        override fun parse(r: JSONObject) = r.getInt("response")

        companion object {
            const val REQUEST_PORN = 0
            const val REQUEST_SPAM = 1
            const val REQUEST_INSULT = 2
            const val REQUEST_ADVERTISEMENT = 3

            private const val TYPE_PORN = "porn"
            private const val TYPE_SPAM = "spam"
            private const val TYPE_INSULT = "insult"
            private const val TYPE_ADVERTISEMENT = "advertisеment"
        }
    }
    /**
     * Date: 04.04.2021
     * Author: Nikita Romanov
     * User actions: [https://vk.com/dev/users.search]
     */
    class Search(query: String, count: Int): VKRequest<UserResponse>("users.search") {
        private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100)

        init {
            addParam("q", query)
            addParam("count", count)
            addParam("offset",0)
            addParam("fields", fields.joinToString(","))
        }

        override fun parse(r: JSONObject): UserResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, UserResponse::class.java)
        }
    }
}
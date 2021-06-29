package com.springsoftware.network.api.request

import com.google.gson.Gson
import com.springsoftware.network.api.models.User
import com.springsoftware.network.api.models.UserResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

object FriendsApi {
    const val REQUEST_USERS_COUNT = 60

    /**
     * Date: 29.04.2021
     * Author: Nikita Romanov
     * Friends actions: [https://vk.com/dev/friends.search]
     */
    class Search(uid: Int, q: String, count: Int = REQUEST_USERS_COUNT): VKRequest<UserResponse>("friends.search") {
        init {
            addParam("user_id", uid)
            addParam("q", q)
            addParam("fields", UserHelper.defaultParamsList())
            addParam("count", count)
        }

        override fun parse(r: JSONObject): UserResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, UserResponse::class.java)
        }
    }
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * Friends actions: [https://vk.com/dev/friends.getSuggestions]
     */
    class GetSuggestions(offset: Int, count: Int = REQUEST_USERS_COUNT): VKRequest<UserResponse>("friends.getSuggestions") {
        init {
            addParam("count", count)
            addParam("offset", offset)
            addParam("fields", UserHelper.defaultParamsList())
        }
        override fun parse(r: JSONObject): UserResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, UserResponse::class.java)
        }
    }
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * Friends actions: [https://vk.com/dev/friends.getRequests]
     */
    class GetRequests(out: Int, offset: Int, count: Int = REQUEST_USERS_COUNT): VKRequest<UserResponse>("friends.getRequests") {
        init {
            addParam("offset", offset)
            addParam("count", count)
            addParam("extended",1)
            addParam("need_mutual",0)
            addParam("out", out)
            addParam("fields", UserHelper.defaultParamsList())
        }
        override fun parse(r: JSONObject): UserResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, UserResponse::class.java)
        }
    }
    /**
     * Date: 19.05.2021
     * Author: Nikita Romanov
     * Get mutual: [https://vk.com/dev/friends.getMutual]
     * Get user info [https://vk.com/dev/users.get]
     */
    class GetMutual(
        private val id: Int,
        private val order: String,
        private val count: Int = REQUEST_USERS_COUNT
    ): ApiCommand<List<User>>() {
        override fun onExecute(manager: VKApiManager): List<User> {
            val userIds = getMutualIds(manager)

            return usersRequest(userIds, manager)
        }
        /* This method create VK request to get mutual ids */
        private fun getMutualIds(manager: VKApiManager): List<Int> {
            val mutualCall = VKMethodCall.Builder()
                .method("friends.getMutual")
                .args("target_uid", id)
                .args("order", order)
                .args("count", count)
                .args("offset", 0)
                .version(manager.config.version)
                .build()
            return manager.execute(mutualCall, UserIdsInfoParser())
        }
        /* This method get create request to get user info */
        private fun usersRequest(ids: List<Int>, manager: VKApiManager): List<User> {
            val usersGetCall = VKMethodCall.Builder()
                .method("users.get")
                .args("user_ids", ids.joinToString(","))
                .args("fields", UserHelper.defaultParamsList())
                .version(manager.config.version)
                .build()

            return manager.execute(usersGetCall, UsersParser())
        }

        /* Parse response and get user ids */
        private class UserIdsInfoParser : VKApiResponseParser<List<Int>> {
            override fun parse(response: String): List<Int> {
                val r = JSONObject(response).getJSONArray("response")
                val ids = arrayListOf<Int>()

                for (i in 0 until r.length()) ids.add(r.getInt(i))
                return ids
            }
        }
        /* Parse response after getting ids */
        private class UsersParser: VKApiResponseParser<List<User>> {
            override fun parse(response: String): List<User> {
                val users = JSONObject(response).getJSONArray("response")
                val result = ArrayList<User>()

                for (i in 0 until users.length())
                    result.add(Gson().fromJson(users.getJSONObject(i).toString(),
                        User::class.java))

                return result
            }
        }

        companion object {
            const val ORDER_TYPE_RANDOM = "random"
            const val ORDER_TYPE_DEFAULT = ""
        }
    }
    /**
     * Date: 29.04.2021
     * Author: Nikita Romanov
     * Friends actions: [https://vk.com/dev/friends.get]
     */
    class Get(uid: Int, order: String, offset: Int, count: Int = REQUEST_USERS_COUNT): VKRequest<UserResponse>("friends.get") {
        init {
            addParam("user_id", uid)
            addParam("order", order)
            addParam("count", count)
            addParam("offset", offset)
            addParam("fields", UserHelper.defaultParamsList())
        }

        override fun parse(r: JSONObject): UserResponse {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, UserResponse::class.java)
        }

        companion object {
            const val ORDER_NAME = "name"
            const val ORDER_HINTS = "hints"
            const val ORDER_RANDOM = "random"
        }
    }
    /**
     * Date: 03.02.2021
     * Author: Nikita Romanov
     * Friends actions: [https://vk.com/dev/friends.add]
     * [https://vk.com/dev/friends.delete]
     */
    class ChangeState (id: Int, state: Int): VKRequest<Int>(
        if (state == STATE_NOT_FRIEND || state == STATE_IS_SUBSCRIBED) "friends.add"
        else "friends.delete"
    ) {
        init {
            addParam("user_id", id)
        }

        override fun parse(r: JSONObject) = if (r.has("response")) 1 else 0

        companion object {
            private const val STATE_NOT_FRIEND = 0
            private const val STATE_IS_SUBSCRIBED = 2
        }
    }
}
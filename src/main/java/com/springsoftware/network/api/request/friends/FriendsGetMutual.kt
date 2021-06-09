package com.springsoftware.network.api.request.friends

import com.google.gson.Gson
import com.springsoftware.network.api.models.User
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject
import java.util.ArrayList

/**
 * Date: 19.05.2021
 * Author: Nikita Romanov
 * Get mutual: [https://vk.com/dev/friends.getMutual]
 * Get user info [https://vk.com/dev/users.get]
 */

class FriendsGetMutual(
        private val id: Int,
        private val count: Int,
        private val order: String): ApiCommand<List<User>>() {
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
        val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.city,
                UserHelper.photo100)
        val usersGetCall = VKMethodCall.Builder()
            .method("users.get")
            .args("user_ids", ids.joinToString(","))
            .args("fields", fields.joinToString(","))
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
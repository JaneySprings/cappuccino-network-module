package com.springsoftware.network.api.request.account

import com.springsoftware.network.api.models.VKUser
import com.springsoftware.network.api.request.fields.UserHelper
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject
import java.util.ArrayList

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Account actions: [https://vk.com/dev/account.getBanned]
 */

class AccountGetBanned(private val count: Int, private val offset: Int): ApiCommand<List<VKUser>>() {
    override fun onExecute(manager: VKApiManager): List<VKUser> {
        val userIds = getUserIds(count, offset, manager)

        return getUsers(userIds, manager)
    }
    /* This method create VK request to get banned ids */
    private fun getUserIds(count: Int, offset: Int, manager: VKApiManager): List<Int> {
        val usersCall = VKMethodCall.Builder()
                .method("account.getBanned")
                .args("offset", offset)
                .args("count", count)
                .version(manager.config.version)
                .build()
        return manager.execute(usersCall, UsersIdsParser())
    }
    /* This method post photo to server url and get its hash, then set its to chat photo */
    private fun getUsers(userIds: List<Int>, manager: VKApiManager): List<VKUser> {
        val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100,
                UserHelper.status)

        val usersTask = VKMethodCall.Builder()
                .method("users.get")
                .args("user_ids", userIds.joinToString(","))
                .args("fields", fields)
                .version(manager.config.version)
                .build()

        return manager.execute(usersTask, UsersParser())
    }

    /* Parse response and get user ids */
    private class UsersIdsParser : VKApiResponseParser<List<Int>> {
        override fun parse(response: String): List<Int> {
            val items = JSONObject(response).getJSONObject("response").getJSONArray("items")
            val result = arrayListOf<Int>()

            for (i in 0 until items.length())
                result.add(items.optInt(i,0))

            return result
        }
    }
    /* Parse response and get all user models */
    private class UsersParser: VKApiResponseParser<List<VKUser>> {
        override fun parse(response: String): List<VKUser> {
            val users = JSONObject(response).getJSONArray("response")
            val result = ArrayList<VKUser>()

            for (i in 0 until users.length())
                result.add(VKUser.parse(users.getJSONObject(i)))

            return result
        }
    }
}
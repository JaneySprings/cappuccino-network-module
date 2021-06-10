package com.springsoftware.network.api.request

import com.google.gson.Gson
import com.springsoftware.network.api.models.Counter
import com.springsoftware.network.api.models.UserResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

object AccountApi {
    /**
     * Date: 02.02.2021
     * Author: Nikita Romanov
     * Account actions: [https://vk.com/dev/account.ban]
     * [https://vk.com/dev/account.unban]
     */
    class Ban(ownerId: Int, isBanned: Boolean): VKRequest<Int>(
        if (isBanned) "account.unban" else "account.ban") {
        init { addParam("owner_id", ownerId) }

        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Account actions: [https://vk.com/dev/account.getBanned]
     */
    class GetBanned(private val count: Int, private val offset: Int): ApiCommand<UserResponse>() {
        override fun onExecute(manager: VKApiManager): UserResponse {
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
        private fun getUsers(userIds: List<Int>, manager: VKApiManager): UserResponse {
            val usersTask = VKMethodCall.Builder()
                .method("users.get")
                .args("user_ids", userIds.joinToString(","))
                .args("fields", UserHelper.defaultParamsList())
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
        private class UsersParser: VKApiResponseParser<UserResponse> {
            override fun parse(response: String): UserResponse {
                val json = JSONObject(response).getJSONObject("response").toString()
                return Gson().fromJson(json, UserResponse::class.java)
            }
        }
    }
    /**
     * Date: 29.05.2021
     * Author: Nikita Romanov
     * Account actions: [https://vk.com/dev/account.getCounters]
     */
    class GetCounters: VKRequest<Counter>("account.getCounters") {
        override fun parse(r: JSONObject): Counter {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, Counter::class.java)
        }
    }
    /**
     * Date: 02.02.2021
     * Author: Nikita Romanov
     * Account actions: [https://vk.com/dev/account.registerDevice]
     */
    class RegisterDevice(private val token: String): ApiCommand<Int>() {
        override fun onExecute(manager: VKApiManager): Int {
            val callBuilder = VKMethodCall.Builder()
                .method("account.registerDevice")
                .args("token", token)
                .args("device_id", manager.config.deviceId)
                .args("settings", PUSH_SETTING)
                .version(manager.config.version)
            return manager.execute(callBuilder.build(), ResponseApiParser())
        }

        private class ResponseApiParser : VKApiResponseParser<Int> {
            override fun parse(response: String) = JSONObject(response).getInt("response")
        }

        companion object {
            private const val PUSH_SETTING = "{\"msg\":[\"on\"],\"chat\":[\"no_sound\"],\"friend\":[\"on\"]}"
        }
    }
    /**
     * Date: 03.04.2021
     * Author: Nikita Romanov
     * Account actions: [https://vk.com/dev/account.saveProfileInfo]
     */
    class SaveProfileInfo(
        screenName: String,
        firstName: String,
        lastName: String,
        status: String,
        bDate: String
    ): VKRequest<Int>("account.saveProfileInfo") {
        init {
            if (firstName.isNotEmpty()) addParam("first_name", firstName)
            if (lastName.isNotEmpty()) addParam("last_name", lastName)
            if (screenName.isNotEmpty()) addParam("screen_name", screenName)
            if (bDate.isNotEmpty()) addParam("bdate", bDate)
            if (status.isNotEmpty()) addParam("status", status)
        }
        override fun parse(r: JSONObject) = r.getJSONObject("response").getInt("changed")
    }
    /**
     * Date: 04.04.2021
     * Author: Nikita Romanov
     * Account actions: [https://vk.com/dev/account.setSilenceMode]
     */
    class SetSilenceMode(id: Int, isEnabled: Boolean): VKRequest<Int>("account.setSilenceMode") {
        init {
            addParam("time", if (isEnabled) -1 else 10)
            addParam("sound", if (isEnabled) 0 else 1)
            addParam("peer_id", id)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 02.02.2021
     * Author: Nikita Romanov
     * Account actions: [https://vk.com/dev/account.unregisterDevice]
     */
    class UnregisterDevice: ApiCommand<Int>() {
        override fun onExecute(manager: VKApiManager): Int {
            val callBuilder = VKMethodCall.Builder()
                .method("account.unregisterDevice")
                .args("device_id", manager.config.deviceId)
                .version(manager.config.version)
            return manager.execute(callBuilder.build(), ResponseApiParser())
        }

        private class ResponseApiParser : VKApiResponseParser<Int> {
            override fun parse(response: String) = JSONObject(response).getInt("response")
        }
    }
}
package com.springsoftware.network.api.request

import android.net.Uri
import com.google.gson.Gson
import com.springsoftware.network.api.mappers.toConversationItems
import com.springsoftware.network.api.mappers.toMessageItems
import com.springsoftware.network.api.models.*
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

object MessagesApi {
    const val LONG_POLL_VERSION = 3
    const val CHAT_ID_DELTA = 2000000000

    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.addChatUser]
     */
    class AddChatUser(chatId: Int, id: Int): VKRequest<Int>("messages.addChatUser") {
        init {
            addParam("chat_id", chatId)
            addParam("user_id", id)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.createChat]
     */
    class CreateChat(title: String): VKRequest<Int>("messages.createChat") {
        init { addParam("title", title) }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.delete]
     */
    class Delete(private val ids: List<Int>, forAll: Int): VKRequest<List<Int>>("messages.delete") {
        init {
            addParam("message_ids", ids.joinToString(","))
            addParam("spam", 0)
            addParam("delete_for_all", forAll)
        }

        override fun parse(r: JSONObject): List<Int> {
            val response = r.getJSONObject("response")
            val result = arrayListOf<Int>()

            for (i in ids) result.add(response.optInt(i.toString(), 0))

            return result
        }

        private class ResponseApiParser: VKApiResponseParser<Int> {
            override fun parse(response: String) =
                if (JSONObject(response).has("response")) 1 else 0
        }
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.deleteConversation]
     */
    class DeleteConversation(id: Int): VKRequest<Int>("messages.deleteConversation") {
        init {
            if (id in 0 until CHAT_ID_DELTA) addParam("user_id", id)
            else addParam("peer_id", id)
        }
        override fun parse(r: JSONObject)
            = r.getJSONObject("response").optInt("last_deleted_id",0)
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.editChat]
     */
    class EditChat(id: Int, title: String): VKRequest<Int>("messages.editChat") {
        init {
            addParam("chat_id", id)
            addParam("title", title)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.getChat]
     */
    class GetChat(id: Int): VKRequest<Chat>("messages.getChat") {
        init {
            addParam("chat_id", id)
            addParam("fields", UserHelper.defaultParamsList())
        }
        override fun parse(r: JSONObject): Chat {
            val response = r.getJSONObject("response").toString()
            return Gson().fromJson(response, Chat::class.java)
        }
    }
    /**
     * Date: 02.05.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.getConversations]
     */
    class GetConversations(count: Int, offset: Int): VKRequest<ConversationsDTO>("messages.getConversations") {
        init {
            addParam("offset", offset)
            addParam("count", count)
            addParam("filter","all")
            addParam("extended", 1)
            addParam("fields", UserHelper.defaultParamsList())
        }

        override fun parse(r: JSONObject): ConversationsDTO {
            val response = r.getJSONObject("response").toString()
            val dto = Gson().fromJson(response, ConversationsResponse::class.java)

            return ConversationsDTO(dto.toConversationItems(), dto.count)
        }
    }
    /**
     * Date: 04.05.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.getConversationsById]
     */
    class GetConversationsById(ids: List<Int>): VKRequest<ConversationsDTO>("messages.getConversationsById") {
        private val fields = listOf(UserHelper.lastSeen, UserHelper.online, UserHelper.photo100)

        init {
            addParam("peer_ids", ids.joinToString(","))
            addParam("extended", 1)
            addParam("fields", fields.joinToString(","))
        }

        override fun parse(r: JSONObject): ConversationsDTO {
            val response = r.getJSONObject("response").toString()
            val dto = Gson().fromJson(response, ConversationsByIdResponse::class.java)

            return ConversationsDTO(dto.toConversationItems(), dto.count)
        }
    }
    /**
     * Date: 21.04.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.getHistory]
     */
    class GetHistory(uid: Int, count: Int, offset: Int): VKRequest<MessagesDTO>("messages.getHistory") {
        init {
            addParam("offset", offset)
            addParam("count", count)
            addParam("peer_id", uid)
            addParam("rev",0)
            addParam("extended",1)
        }
        override fun parse(r: JSONObject): MessagesDTO {
            val response = r.getJSONObject("response").toString()
            val dto = Gson().fromJson(response, MessagesResponse::class.java)
            return MessagesDTO(dto.toMessageItems(), dto.count)
        }
    }
    /**
     * Date: 23.05.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.getHistoryAttachments]
     */
    class GetHistoryAttachments(
        uid: Int,
        count: Int,
        shift: String,
        type: String
        ): VKRequest<AttachmentResponse>("messages.getHistoryAttachments") {
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
    /**
     * Date: 10.03.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.getLongPollHistory]
     */
    class GetLongPollHistory(keys: Array<Int>): VKRequest<LongPollDTO>("messages.getLongPollHistory") {
        init {
            addParam("ts", keys[0])
            addParam("pts", keys[1])
            addParam("fields", UserHelper.defaultParamsList())
            addParam("lp_version", LONG_POLL_VERSION)
        }
        override fun parse(r: JSONObject): LongPollDTO {
            val response = r.getJSONObject("response").toString()
            val dto = Gson().fromJson(response, LongPollResponse::class.java)
            val messages = java.util.ArrayList<MessageItem>()

            for (i in dto.messages.items.indices)
                messages.add(MessageItem.fromResponse(dto.messages.items[i], dto.profiles, dto.groups))

            return LongPollDTO(dto.history, dto.toMessageItems(), dto.newPts)
        }
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.getLongPollServer]
     */
    class GetLongPollServer: VKRequest<List<Int>>("messages.getLongPollServer") {
        init {
            addParam("need_pts", 1)
            addParam("lp_version", LONG_POLL_VERSION)
        }
        override fun parse(r: JSONObject): List<Int> {
            val keys = r.getJSONObject("response")
            return listOf(keys.getInt("ts"), keys.getInt("pts"))
        }
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.markAsRead]
     */
    class MarkAsRead(peerId: Int): VKRequest<Int>("messages.markAsRead") {
        init {
            addParam("peer_id", peerId)
            addParam("mark_conversation_as_read", 1)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.removeChatUser]
     */
    class RemoveChatUser(chatId: Int, id: Int): VKRequest<Int>("messages.removeChatUser") {
        init {
            addParam("chat_id", chatId)
            addParam("user_id", id)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.send]
     * Get upload server url [https://vk.com/dev/photos.getMessagesUploadServer]
     * Save photo and get data [https://vk.com/dev/photos.saveMessagesPhoto]
     */
    class Send(
        private val uid: Int,
        private val text: String,

        private val photos: List<Uri>,
        private val data: List<String>,
        private val forwarded: List<Int>): ApiCommand<Int>() {
        private val uploadData: ArrayList<String> = arrayListOf()
        override fun onExecute(manager: VKApiManager): Int {
            val callBuilder = VKMethodCall.Builder()
                .method("messages.send")
                .args("random_id", Random().nextInt())
                .args("peer_id", uid)
                .args("message", text)
                .version(manager.config.version)

            if (forwarded.isNotEmpty())
                callBuilder.args("forward_messages", forwarded.chunked(FORWARDED_LIMIT)[0]
                    .joinToString(","))

            if (photos.isNotEmpty()) {
                val uploadInfo = getServerUploadInfo(manager)

                for (i in photos.indices)
                    uploadData.add(uploadPhoto(photos[i], uploadInfo, manager))
            }
            if (data.isNotEmpty())
                for (i in data.indices) uploadData.add(data[i])

            if (uploadData.isNotEmpty())
                callBuilder.args("attachment", uploadData.joinToString(","))

            return manager.execute(callBuilder.build(), ResponseApiParser())
        }

        /* This method create VK request to get upload url */
        private fun getServerUploadInfo(manager: VKApiManager): String {
            val uploadInfoCall = VKMethodCall.Builder()
                .method("photos.getMessagesUploadServer")
                .args("peer_id", uid)
                .version(manager.config.version)
                .build()
            return manager.execute(uploadInfoCall, ServerUploadInfoParser())
        }
        /* This method post photo to server url and get its info, then save it and give its data */
        private fun uploadPhoto(uri: Uri, serverUploadUrl: String, manager: VKApiManager): String {
            val fileUploadCall = VKHttpPostCall.Builder()
                .url(serverUploadUrl)
                .args("photo", uri, "image.jpg")
                .timeout(TimeUnit.MINUTES.toMillis(5))
                .retryCount(RETRY_COUNT)
                .build()
            val fileUploadInfo = manager.execute(fileUploadCall, null, FileUploadInfoParser())

            val saveCall = VKMethodCall.Builder()
                .method("photos.saveMessagesPhoto")
                .args("server", fileUploadInfo.server)
                .args("photo", fileUploadInfo.photo)
                .args("hash", fileUploadInfo.hash)
                .version(manager.config.version)
                .build()

            val saveInfo = manager.execute(saveCall, SaveInfoParser())

            return saveInfo.getAttachment()
        }

        /* Parse response and get upload server url */
        private class ServerUploadInfoParser : VKApiResponseParser<String> {
            override fun parse(response: String) = JSONObject(response).getJSONObject("response")
                .optString("upload_url","") ?: ""
        }
        /* Parse post-call response and get uploaded photo info */
        private class FileUploadInfoParser: VKApiResponseParser<VKFileUploadInfo> {
            override fun parse(response: String): VKFileUploadInfo {
                val joResponse = JSONObject(response)
                return VKFileUploadInfo(
                    server = joResponse.getString("server"),
                    photo = joResponse.getString("photo"),
                    hash = joResponse.getString("hash")
                )
            }
        }
        /* Parse response after saving photo and get its data */
        private class SaveInfoParser: VKApiResponseParser<VKSaveInfo> {
            override fun parse(response: String): VKSaveInfo {
                val joResponse = JSONObject(response).getJSONArray("response").getJSONObject(0)
                return VKSaveInfo(
                    id = joResponse.getInt("id"),
                    ownerId = joResponse.getInt("owner_id")
                )
            }
        }
        /* Parse response after success sending message */
        private class ResponseApiParser : VKApiResponseParser<Int> {
            override fun parse(response: String) = JSONObject(response).getInt("response")
        }

        companion object {
            private const val FORWARDED_LIMIT = 500
            private const val RETRY_COUNT = 3
        }
    }
    /**
     * Date: 04.02.2021
     * Author: Nikita Romanov
     * Messages actions: [https://vk.com/dev/messages.setChatPhoto]
     * Get upload server url [https://vk.com/dev/photos.getChatUploadServer]
     */
    class SetChatPhoto(private val chatId: Int, private val photoUri: Uri): ApiCommand<Int>() {
        override fun onExecute(manager: VKApiManager): Int {
            val uploadInfo = getServerUploadInfo(manager)

            return uploadPhoto(photoUri, uploadInfo, manager)
        }
        /* This method create VK request to get upload url */
        private fun getServerUploadInfo(manager: VKApiManager): String {
            val uploadInfoCall = VKMethodCall.Builder()
                .method("photos.getChatUploadServer")
                .args("chat_id", chatId)
                .version(manager.config.version)
                .build()
            return manager.execute(uploadInfoCall, ServerUploadInfoParser())
        }
        /* This method post photo to server url and get its hash, then set its to chat photo */
        private fun uploadPhoto(uri: Uri, serverUploadInfo: String, manager: VKApiManager): Int {
            val fileUploadCall = VKHttpPostCall.Builder()
                .url(serverUploadInfo)
                .args("photo", uri, "image.jpg")
                .timeout(TimeUnit.MINUTES.toMillis(5))
                .retryCount(RETRY_COUNT)
                .build()
            val fileUploadHash = manager.execute(fileUploadCall, null, FileUploadInfoParser())

            val saveCall = VKMethodCall.Builder()
                .method("messages.setChatPhoto")
                .args("file", fileUploadHash)
                .version(manager.config.version)
                .build()

            return manager.execute(saveCall, SaveInfoParser())
        }

        /* Parse response and get upload server url */
        private class ServerUploadInfoParser : VKApiResponseParser<String> {
            override fun parse(response: String) = JSONObject(response).getJSONObject("response")
                .optString("upload_url","") ?: ""
        }
        /* Parse post-call response and get uploaded photo hash */
        private class FileUploadInfoParser: VKApiResponseParser<String> {
            override fun parse(response: String) = JSONObject(response)
                .optString("response","") ?: ""
        }
        /* Get success int after uploading and apply photo */
        private class SaveInfoParser: VKApiResponseParser<Int> {
            override fun parse(response: String) = JSONObject(response)
                .getJSONObject("response").getInt("message_id")
        }

        companion object {
            private const val RETRY_COUNT = 3
        }
    }
}
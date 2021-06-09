package com.springsoftware.network.api.request.messages

import android.net.Uri
import com.springsoftware.network.api.models.VKFileUploadInfo
import com.springsoftware.network.api.models.VKSaveInfo
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.send]
 * Get upload server url [https://vk.com/dev/photos.getMessagesUploadServer]
 * Save photo and get data [https://vk.com/dev/photos.saveMessagesPhoto]
 */

class MessagesSend(
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
        const val FORWARDED_LIMIT = 500
        const val RETRY_COUNT = 3
    }
}


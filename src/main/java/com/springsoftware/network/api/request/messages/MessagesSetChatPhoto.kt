package com.springsoftware.network.api.request.messages

import android.net.Uri
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Date: 04.02.2021
 * Author: Nikita Romanov
 * Messages actions: [https://vk.com/dev/messages.setChatPhoto]
 * Get upload server url [https://vk.com/dev/photos.getChatUploadServer]
 */

class MessagesSetChatPhoto(private val chatId: Int, private val photoUri: Uri): ApiCommand<Int>() {
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
        const val RETRY_COUNT = 3
    }
}
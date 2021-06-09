package com.springsoftware.network.api.request

import android.net.Uri
import com.google.gson.Gson
import com.springsoftware.network.api.models.PhotosResponse
import com.springsoftware.network.api.models.VKFileUploadInfo
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object PhotosApi {
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * Photos actions: [https://vk.com/dev/photos.copy]
     */
    class Copy(ownerId: Int, photoId: Int, key: String): VKRequest<Int>("photos.copy") {
        init {
            addParam("owner_id", ownerId)
            addParam("photo_id", photoId)
            addParam("access_key", key)
        }
        override fun parse(r: JSONObject) = r.getInt("response")
    }
    /**
     * Date: 06.04.2021
     * Author: Nikita Romanov
     * Photos actions: [https://vk.com/dev/photos.get]
     */
    class Get(uid: Int, aid: String, count: Int, offset: Int): VKRequest<PhotosResponse>("photos.get") {
        init {
            addParam("owner_id", uid)
            addParam("album_id", aid)
            addParam("rev", 1)
            addParam("extended",1)
            addParam("offset", offset)
            addParam("count", count)
        }

        override fun parse(r: JSONObject): PhotosResponse {
            val response = r.getJSONObject("response").toString()

            return Gson().fromJson(response, PhotosResponse::class.java)
        }

        companion object {
            const val FILTER_WALL = "wall"
            const val FILTER_PROFILE = "profile"
            const val FILTER_SAVED = "saved"
        }
    }
    /**
     * Date: 10.05.2021
     * Author: Nikita Romanov
     * Photos actions: [https://vk.com/dev/photos.getAll]
     */
    class GetAll(uid: Int, offset: Int, count: Int): VKRequest<PhotosResponse>("photos.getAll") {
        init {
            addParam("owner_id", uid)
            addParam("extended",1)
            addParam("offset",offset)
            addParam("count", count)
        }

        override fun parse(r: JSONObject): PhotosResponse {
            val response = r.getJSONObject("response").toString()

            return Gson().fromJson(response, PhotosResponse::class.java)
        }
    }
    /**
     * Date: 05.02.2021
     * Author: Nikita Romanov
     * Photos actions: [https://vk.com/dev/photos.saveOwnerPhoto]
     * Get upload url [https://vk.com/dev/photos.getOwnerPhotoUploadServer]
     */
    class SaveOwnerPhoto(private val photoUri: Uri): ApiCommand<Int>() {
        override fun onExecute(manager: VKApiManager): Int {
            val uploadInfo = getServerUploadInfo(manager)

            return uploadPhoto(photoUri, uploadInfo, manager)
        }
        /* This method create VK request to get upload url */
        private fun getServerUploadInfo(manager: VKApiManager): String {
            val uploadInfoCall = VKMethodCall.Builder()
                .method("photos.getOwnerPhotoUploadServer")
                .version(manager.config.version)
                .build()
            return manager.execute(uploadInfoCall, ServerUploadInfoParser())
        }
        /* This method post photo to server url and get its info, then save it and give its data */
        private fun uploadPhoto(uri: Uri, serverUploadUrl: String, manager: VKApiManager): Int {
            val fileUploadCall = VKHttpPostCall.Builder()
                .url(serverUploadUrl)
                .args("photo", uri, "image.jpg")
                .timeout(TimeUnit.MINUTES.toMillis(5))
                .retryCount(RETRY_COUNT)
                .build()
            val fileUploadInfo = manager.execute(fileUploadCall, null, FileUploadInfoParser())

            val saveCall = VKMethodCall.Builder()
                .method("photos.saveOwnerPhoto")
                .args("server", fileUploadInfo.server)
                .args("photo", fileUploadInfo.photo)
                .args("hash", fileUploadInfo.hash)
                .version(manager.config.version)
                .build()

            return manager.execute(saveCall, SaveInfoParser())
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
        private class SaveInfoParser: VKApiResponseParser<Int> {
            override fun parse(response: String) = JSONObject(response)
                .getJSONObject("response").getInt("saved")
        }

        companion object {
            const val RETRY_COUNT = 3
        }
    }
}
package com.springsoftware.network.api.models

import android.os.Parcelable

data class VKSaveInfo(val id: Int, val ownerId: Int) {
    fun getAttachment() = "photo${ownerId}_$id"
}
data class VKFileUploadInfo(val server: String, val photo: String, val hash: String)

data class VKResponseNext(val data: List<Parcelable>, val nextFrom: String)
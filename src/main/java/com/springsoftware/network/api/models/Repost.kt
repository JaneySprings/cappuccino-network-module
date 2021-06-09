package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class RepostItem(
    val id: Int,
    val ownerId: Int,
    val date: Int,
    val photo: String,
    val name: String,
    val text: String,

    val photos: List<Photo>,
    val videos: List<Video>,
    val docs: List<Document>,
    val link: Link?
) {
    companion object {
        fun fromResponse(item: Repost, profiles: List<User>?, groups: List<Group>?): RepostItem {
            val photos: ArrayList<Photo> = arrayListOf()
            val videos: ArrayList<Video> = arrayListOf()
            val docs: ArrayList<Document> = arrayListOf()
            var link: Link? = null
            val name: String
            val photo: String

            if (item.ownerId > 0) {
                val source = profiles!!.find { item.ownerId == it.id }
                name = "${source?.firstName} ${source?.lastName}"
                photo = source?.photo100 ?: ""
            } else {
                val source = groups!!.find { item.ownerId == -it.id }
                name = source?.name ?: ""
                photo = source?.photo100 ?: ""
            }

            for (i in 0 until (item.attachments?.size ?: 0)) when (item.attachments!![i].type) {
                "photo" -> photos.add(item.attachments[i].photo!!)
                "video" -> videos.add(item.attachments[i].video!!)
                "doc" -> docs.add(item.attachments[i].doc!!)
                "link" -> link = item.attachments[i].link
            }

            return RepostItem(
                id = item.id,
                ownerId = item.ownerId,
                date = item.date,
                photo = photo,
                name = name,
                text = item.text,
                photos = photos,
                videos = videos,
                docs = docs,
                link = link
            )
        }
    }
}

data class Repost(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id", alternate = ["from_id"]) val ownerId: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("post_type") val postType: String,
    @SerializedName("text") val text: String,
    @SerializedName("attachments") val attachments: List<Attachment>?
)
package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class NewsfeedEntity(val posts: List<PostItem>, val nextFrom: String)

data class PostItem(
    val id: Int,
    val sourceId: Int,
    val likes: Int,
    val reposts: Int,
    val date: Int,
    val photo: String,
    val name: String,
    val text: String,
    val userLikes: Boolean,

    val photos: List<Photo>,
    val videos: List<Video>,
    val docs: List<Document>,
    val post: RepostItem?,
    val link: Link?
) {
    companion object {
        fun fromResponse(item: Post, profiles: List<User>?, groups: List<Group>?): PostItem {
            val photos: ArrayList<Photo> = arrayListOf()
            val videos: ArrayList<Video> = arrayListOf()
            val docs: ArrayList<Document> = arrayListOf()
            var post: RepostItem? = null
            var link: Link? = null
            val name: String
            val photo: String

            if (item.sourceId > 0) {
                val source = profiles!!.find { item.sourceId == it.id }
                name = "${source?.firstName} ${source?.lastName}"
                photo = source?.photo100 ?: ""
            } else {
                val source = groups!!.find { item.sourceId == -it.id }
                name = source?.name ?: ""
                photo = source?.photo100 ?: ""
            }

            for (i in 0 until (item.attachments?.size ?: 0)) when (item.attachments!![i].type) {
                "photo" -> photos.add(item.attachments[i].photo!!)
                "video" -> videos.add(item.attachments[i].video!!)
                "doc" -> docs.add(item.attachments[i].doc!!)
                "link" -> link = item.attachments[i].link
            }

            if (!item.copyHistory.isNullOrEmpty())
                post = RepostItem.fromResponse(item.copyHistory[0], profiles, groups)

            return PostItem(
                id = item.postId,
                sourceId = item.sourceId,
                likes = item.likes?.count ?: 0,
                userLikes = (item.likes?.userLikes ?: 0 == 1),
                reposts = item.reposts?.count ?: 0,
                date = item.date,
                photo = photo,
                name = name,
                text = item.text,
                photos = photos,
                videos = videos,
                docs = docs,
                link = link,
                post = post
            )
        }
    }
}

data class PostResponse(
    @SerializedName("items") val items: List<Post>,
    @SerializedName("profiles") val profiles: List<User>?,
    @SerializedName("groups") val groups: List<Group>?,
    @SerializedName("next_from") val nextFrom: String
)
data class Post(
    @SerializedName("source_id", alternate = ["from_id"]) val sourceId: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("can_doubt_category") val canDoubtCategory: Boolean,
    @SerializedName("can_set_category") val canSetCategory: Boolean,
    @SerializedName("post_type") val postType: String,
    @SerializedName("text") val text: String,
    @SerializedName("copy_history") val copyHistory: List<Repost>?,
    @SerializedName("can_edit") val canEdit: Int,
    @SerializedName("can_delete") val canDelete: Int,
    @SerializedName("can_archive") val canArchive: Boolean,
    @SerializedName("is_archived") val isArchived: Boolean,
    @SerializedName("attachments") val attachments: List<Attachment>?,
    @SerializedName("comments") val comments: Comments?,
    @SerializedName("likes") val likes: Likes?,
    @SerializedName("reposts") val reposts: Reposts?,
    @SerializedName("views") val views: Views?,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("short_text_rate") val shortTextRate: Float,
    @SerializedName("post_id", alternate = ["id"]) val postId: Int,
    @SerializedName("type") val type: String
)
data class Comments(
    @SerializedName("count") val count: Int,
    @SerializedName("can_post") val can_post: Int
)
data class Likes(
    @SerializedName("count") val count: Int,
    @SerializedName("user_likes") val userLikes: Int,
    @SerializedName("can_like") val canLike: Int,
    @SerializedName("canPublish") val canPublish: Int
)
data class Reposts(
    @SerializedName("count") val count: Int,
    @SerializedName("user_reposted") val userReposted: Int
)
data class Views(
    @SerializedName("count") val count: Int
)
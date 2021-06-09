package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class MessageEntity(val messages: List<MessageItem>, val count: Int)

data class MessageItem(
    val peerId: Int,
    val fromId: Int,
    val date: Int,
    val out: Int,
    val id: Int,

    val photo100: String,
    val name: String,
    val text: String,

    val isEdited: Boolean = false,

    val fwdMessages: ArrayList<MessageItem>,
    val audioMessage: AudioMessage?,
    val docs: ArrayList<Document>,
    val photos: ArrayList<Photo>,
    val videos: ArrayList<Video>,
    val audios: ArrayList<Audio>,
    val replyMessage: MessageItem?,
    val graffiti: Graffiti?,
    val sticker: Sticker?,
    val wall: RepostItem?,
    val link: Link?
) {
    companion object {
        fun fromResponse(item: Message, profiles: List<User>?, groups: List<Group>?): MessageItem {
            val forwarded: ArrayList<MessageItem> = arrayListOf()
            val docs: ArrayList<Document> = arrayListOf()
            val photos: ArrayList<Photo> = arrayListOf()
            val videos: ArrayList<Video> = arrayListOf()
            val audios: ArrayList<Audio> = arrayListOf()
            var voice: AudioMessage? = null
            var reply: MessageItem? = null
            var graffiti: Graffiti? = null
            var sticker: Sticker? = null
            var wall: RepostItem? = null
            var link: Link? = null
            val name: String
            val photo: String

            if (item.fromId > 0) {
                val source = profiles!!.find { item.fromId == it.id }
                name = "${source?.firstName} ${source?.lastName}"
                photo = source?.photo100 ?: ""
            } else {
                val source = groups!!.find { item.fromId == -it.id }
                name = source?.name ?: ""
                photo = source?.photo100 ?: ""
            }

            for (i in item.attachments.indices) when (item.attachments[i].type) {
                "photo" -> photos.add(item.attachments[i].photo!!)
                "video" -> videos.add(item.attachments[i].video!!)
                "audio" -> audios.add(item.attachments[i].audio!!)
                "doc" -> docs.add(item.attachments[i].doc!!)
                "graffiti" -> graffiti = item.attachments[i].graffiti
                "sticker" -> sticker = item.attachments[i].sticker
                "link" -> link = item.attachments[i].link
                "wall" -> wall = RepostItem.fromResponse(item.attachments[i].wall!!, profiles, groups)
                "audio_message" -> voice = item.attachments[i].audioMessage
            }

            for (i in 0 until (item.fwdMessages?.size ?: 0))
                forwarded.add(fromResponse(item.fwdMessages!![i], profiles, groups))

            if (item.replyMessage != null)
                reply = fromResponse(item.replyMessage, profiles, groups)

            return MessageItem(
                peerId = item.peerId, fromId = item.fromId, date = item.date, out = item.out,
                id = item.id, photo100 = photo, name = name, text = item.text,
                isEdited = item.updateTime != 0, fwdMessages = forwarded, sticker = sticker,
                graffiti = graffiti, audioMessage = voice, docs = docs, photos = photos,
                videos = videos, audios = audios, replyMessage = reply, wall = wall, link = link
            )
        }
    }
}

data class MessageResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Message>,
    @SerializedName("profiles") val profiles: List<User>?,
    @SerializedName("groups") val groups: List<Group>?
)
data class Message(
    @SerializedName("date") val date: Int,
    @SerializedName("from_id") val fromId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("out") val out: Int,
    @SerializedName("peer_id") val peerId: Int,
    @SerializedName("text") val text: String,
    @SerializedName("conversation_message_id") val conversationMessageId: Int,
    @SerializedName("fwd_messages") val fwdMessages: List<Message>?,
    @SerializedName("reply_message") val replyMessage: Message?,
    @SerializedName("action") val action: Action?,
    @SerializedName("important") val important: Boolean,
    @SerializedName("random_id") val randomId: Int,
    @SerializedName("attachments") val attachments: List<Attachment>,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("update_time") val updateTime: Int
)
data class Action(
    @SerializedName("type") val type: String,
    @SerializedName("member_id") val memberId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("date") val date: Int,
    @SerializedName("photo") val photo: Covers?
)
data class Covers(
    @SerializedName("photo_50") val photo50: String,
    @SerializedName("photo_100") val photo100: String,
    @SerializedName("photo_200") val photo200: String
)
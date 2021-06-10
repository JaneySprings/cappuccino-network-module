package com.springsoftware.network.api.mappers

import com.springsoftware.network.api.models.*
import java.util.ArrayList

/* Create displayable items from list of messages */
fun MessagesResponse.toMessageItems(): List<MessageItem> {
    val result = arrayListOf<MessageItem>()

    for (item in items)
        result.add(mapMessageToItem(item, profiles, groups))

    return result
}

/* Create displayable items from list of messages from long poll */
fun LongPollResponse.toMessageItems(): List<MessageItem> {
    val result = arrayListOf<MessageItem>()

    for (item in messages.items)
        result.add(mapMessageToItem(item, profiles, groups))

    return result
}

private fun mapMessageToItem(item: Message, profiles: List<User>?, groups: List<Group>?): MessageItem {
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
        forwarded.add(MessageItem.fromResponse(item.fwdMessages!![i], profiles, groups))

    if (item.replyMessage != null)
        reply = MessageItem.fromResponse(item.replyMessage, profiles, groups)

    return MessageItem(
        peerId = item.peerId, fromId = item.fromId, date = item.date, out = item.out,
        id = item.id, photo100 = photo, name = name, text = item.text,
        isEdited = item.updateTime != 0, fwdMessages = forwarded, sticker = sticker,
        graffiti = graffiti, audioMessage = voice, docs = docs, photos = photos,
        videos = videos, audios = audios, replyMessage = reply, wall = wall, link = link
    )
}
package com.springsoftware.network.api.mappers

import com.springsoftware.network.api.models.*

/* Create displayable items from list of conversations */
fun ConversationsResponse.toConversationItems(): List<ConversationItem> {
    val items = arrayListOf<ConversationItem>()

    for(item in this.items) {
        val members: Int
        val name: String
        val title: String
        val photo: String
        val online: Boolean
        val lastSeen: LastSeen?

        when (item.conversation.peer.type) {
            "user" -> {
                val source = profiles!!.find { item.conversation.peer.id == it.id }
                title = "${source?.firstName} ${source?.lastName}"
                photo = source?.photo100 ?: ""
                online = source?.online == 1
                lastSeen = source?.lastSeen
                members = 0
                name = if (item.lastMessage.out == 0) ""
                       else ConversationItem.TAG_SELF_SENDER
            }
            "group" -> {
                val source = groups!!.find { item.conversation.peer.id == -it.id }
                title = source?.name ?: ""
                photo = source?.photo100 ?: ""
                online = false
                lastSeen = null
                members = 0
                name = if (item.lastMessage.out == 0) ""
                       else ConversationItem.TAG_SELF_SENDER
            }
            else -> {
                title = item.conversation.chatSettings?.title ?: ""
                photo = item.conversation.chatSettings?.photo?.photo100 ?: ""
                members = item.conversation.chatSettings?.membersCount ?: 0
                online = false
                lastSeen = null
                name = if (item.lastMessage.out == 0) {
                    if (item.lastMessage.fromId < 0) {
                        val source = groups!!.find { item.lastMessage.fromId == -it.id }
                        source?.name ?: ""
                    } else {
                        val source = profiles!!.find { item.lastMessage.fromId == it.id }
                        source?.firstName ?: ""
                    }
                }
                else ConversationItem.TAG_SELF_SENDER
            }
        }

        val text: String = if (item.lastMessage.text.isEmpty() && item.lastMessage.attachments.isNotEmpty())
            item.lastMessage.attachments[0].type
        else item.lastMessage.text


        items.add(ConversationItem(
            disabledPush = item.conversation.pushSettings?.disabled_forever ?: false,
            pinnedMessage = item.conversation.chatSettings?.pinnedMessage,
            isRead = item.lastMessage.id == item.conversation.outRead,
            unreadCount = item.conversation.unreadCount,
            type = item.conversation.peer.type,
            chatId = item.conversation.peer.id,
            date = item.lastMessage.date,
            membersCount = members,
            lastSeen = lastSeen,
            senderName = name,
            photo100 = photo,
            online = online,
            title = title,
            text = text
        ))
    }

    return items
}

/* Create displayable items (for dialog fragment information) from list of conversation */
fun ConversationsByIdResponse.toConversationItems(): List<ConversationItem> {
    val items = arrayListOf<ConversationItem>()

    for(item in this.items) {
        val members: Int
        val title: String
        val photo: String
        val online: Boolean
        val lastSeen: LastSeen?

        when (item.peer.type) {
            "user" -> {
                val source = profiles!!.find { item.peer.id == it.id }
                title = "${source?.firstName} ${source?.lastName}"
                photo = source?.photo100 ?: ""
                online = source?.online == 1
                lastSeen = source?.lastSeen
                members = 0
            }
            "group" -> {
                val source = groups!!.find { item.peer.id == -it.id }
                title = source?.name ?: ""
                photo = source?.photo100 ?: ""
                online = false
                lastSeen = null
                members = 0
            }
            else -> {
                title = item.chatSettings?.title ?: ""
                photo = item.chatSettings?.photo?.photo100 ?: ""
                members = item.chatSettings?.membersCount ?: 0
                online = false
                lastSeen = null
            }
        }

        items.add(ConversationItem(
            disabledPush = item.pushSettings?.disabled_forever ?: false,
            pinnedMessage = item.chatSettings?.pinnedMessage,
            unreadCount = item.unreadCount,
            type = item.peer.type,
            chatId = item.peer.id,
            membersCount = members,
            lastSeen = lastSeen,
            photo100 = photo,
            online = online,
            senderName = "",
            isRead = false,
            title = title,
            date = 0,
            text = ""
        ))
    }

    return items
}
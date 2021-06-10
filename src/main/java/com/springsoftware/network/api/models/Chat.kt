package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("title") val title: String
)

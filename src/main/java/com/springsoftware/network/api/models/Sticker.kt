package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class StickersResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<StickerSet>
)

data class StickerSet(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("purchased") val purchased: Int,
    @SerializedName("active") val active: Int,
    @SerializedName("purchase_date") val purchaseDate: Int,
    @SerializedName("title") val title: String,
    @SerializedName("stickers") val stickers: List<Sticker>,
    @SerializedName("icon") val icon: List<Image>,
    @SerializedName("previews") val previews: List<Image>,
    @SerializedName("has_animation") val hasAnimation: Boolean,
    @SerializedName("is_new") val isNew: Boolean
)

data class Sticker(
    @SerializedName("is_allowed") val isAllowed: Boolean,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("sticker_id") val stickerId: Int,
    @SerializedName("images") val images: List<Image>,
    @SerializedName("images_with_background") val imagesWithBackground: List<Image>,
    @SerializedName("animation_url") val animationUrl: String?
)
package com.springsoftware.network.api.models

import com.google.gson.annotations.SerializedName

data class Sticker(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("sticker_id") val stickerId: Int,
    @SerializedName("images") val images: List<Image>,
    @SerializedName("images_with_background") val imagesWithBackground: List<Image>,
    @SerializedName("animation_url") val animationUrl: String?,
)
package com.jescoding.pixel.jjappandroid.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardItemDto(
    @SerialName("item_sku") val itemSku: String,
    @SerialName("item_name") val itemName: String,
    @SerialName("item_variant") val itemVariant: String,
    @SerialName("available_stock") val availableStock: Int,
    @SerialName("on_hand_stock") val onHandStock: Int,
    @SerialName("on_the_way_stock") val onTheWayStock: Int,
    @SerialName("item_cost_price") val itemCostPrice: Double,
    @SerialName("item_selling_price") val itemSellingPrice: Double,
    @SerialName("item_image_res_id") val itemImageResId: Int,
    @SerialName("item_uri") val itemUri: String? = null,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("is_deleted") val isDeleted: Boolean = false,
    @SerialName("user_id") val userId: String? = null
)

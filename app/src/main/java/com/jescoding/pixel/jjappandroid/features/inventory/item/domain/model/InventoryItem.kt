package com.jescoding.pixel.jjappandroid.features.inventory.item.domain.model

import androidx.annotation.DrawableRes

data class InventoryItem(
    val itemName: String,
    val itemVariant: String,
    val availableStock: Int,
    val onHandStock: Int,
    val onTheWayStock: Int,
    val itemCostPrice: Double,
    val itemSellingPrice: Double,
    @DrawableRes val itemImageResId: Int
)

package com.jescoding.pixel.jjappandroid.core.domain.model

import androidx.annotation.DrawableRes
import java.math.BigDecimal

/**
 * Data class representing an item on the dashboard.
 *
 * @property itemSku The SKU of the item.
 * @property itemName The name of the item.
 * @property itemVariant The variant of the item.
 * @property availableStock The available stock quantity.
 * @property onHandStock The on-hand stock quantity.
 * @property onTheWayStock The stock quantity that is on the way.
 * @property itemCostPrice The cost price of the item.
 * @property itemSellingPrice The selling price of the item.
 * @property itemImageResId The resource ID for the item's image.
 */
data class DashboardItem(
    val itemSku: String,
    val itemName: String,
    val itemVariant: String,
    val availableStock: Int,
    val onHandStock: Int,
    val onTheWayStock: Int,
    val itemCostPrice: BigDecimal,
    val itemSellingPrice: BigDecimal,
    @DrawableRes val itemImageResId: Int
)

package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.data

import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem

object FakeInventoryData {
    val items = listOf(
        DashboardItem(
            itemSku = "MW-CH-V5-CLAMP",
            itemName = "Original Motowolf Cellphone Holder V5",
            itemVariant = "Clamp Type",
            itemImageResId = R.drawable.sample,
            availableStock = 9,
            onHandStock = 9,
            onTheWayStock = 0,
            itemCostPrice = 150.00.toBigDecimal(),
            itemSellingPrice = 199.00.toBigDecimal()
        )
    )
}
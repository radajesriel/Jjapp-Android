package com.jescoding.pixel.jjappandroid.features.inventory.item.presentation.data

import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.item.domain.model.InventoryItem

object FakeInventoryData {
    val items = listOf(
        InventoryItem(
            itemName = "Original Motowolf Cellphone Holder V5",
            itemVariant = "Clamp Type",
            itemImageResId = R.drawable.sample,
            availableStock = 9,
            onHandStock = 9,
            onTheWayStock = 0,
            itemCostPrice = 150.00,
            itemSellingPrice = 199.00
        )
    )
}
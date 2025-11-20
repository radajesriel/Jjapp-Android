package com.jescoding.pixel.jjappandroid.core.data.local.real.data

import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.DashboardItemEntity

object SeedData {
    val items = listOf(
        DashboardItemEntity(
            itemSku = "MW-CH-V5-CLAMP",
            itemName = "Original Motowolf Cellphone Holder V5",
            itemVariant = "Clamp Type",
            itemImageResId = R.drawable.sample,
            availableStock = 10,
            onHandStock = 10,
            onTheWayStock = 0,
            itemCostPrice = 150.00,
            itemSellingPrice = 199.00,
            itemUri = null
        ),
        DashboardItemEntity(
            itemSku = "MW-BH-V3-CLAMP",
            itemName = "Original Motowolf Bottle Holder V3",
            itemVariant = "Clamp Type",
            itemImageResId = R.drawable.sample2,
            availableStock = 5,
            onHandStock = 10,
            onTheWayStock = 0,
            itemCostPrice = 200.00,
            itemSellingPrice = 250.00,
            itemUri = null
        ),
        DashboardItemEntity(
            itemSku = "MW-HBR-28MM-BLK",
            itemName = "Motowolf Handlebar Riser",
            itemVariant = "Black/28mm",
            itemImageResId = R.drawable.sample3,
            availableStock = 22,
            onHandStock = 10,
            onTheWayStock = 0,
            itemCostPrice = 480.00,
            itemSellingPrice = 640.00,
            itemUri = null
        )
    )
}
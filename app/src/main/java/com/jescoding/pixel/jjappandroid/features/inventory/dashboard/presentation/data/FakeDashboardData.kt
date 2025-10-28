package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation.data

import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model.DashboardItem

object FakeDashboardData {
    val items = listOf(
        DashboardItem(
            itemName = "Original Motowolf Cellphone Holder V5",
            itemVariant = "Clamp Type",
            itemImageResId = R.drawable.sample,
            availableStock = 10
        ),
        DashboardItem(
            itemName = "Original Motowolf Bottle Holder V3",
            itemVariant = "Clamp Type",
            itemImageResId = R.drawable.sample2,
            availableStock = 5
        ),
        DashboardItem(
            itemName = "Motowolf Handlebar Riser",
            itemVariant = "Black/28mm",
            itemImageResId = R.drawable.sample3,
            availableStock = 22
        )
    )
}
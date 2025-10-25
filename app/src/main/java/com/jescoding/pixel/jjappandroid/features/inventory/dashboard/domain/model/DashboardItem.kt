package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model

import androidx.annotation.DrawableRes
import com.jescoding.pixel.jjappandroid.R

data class DashboardItem(
    val itemName: String = "Original Motowolf Cellphone Holder V5",
    val itemVariant: String = "Clamp Type",
    val availableStock: Int = 110,
    @DrawableRes val itemImageResId: Int = R.drawable.sample
)

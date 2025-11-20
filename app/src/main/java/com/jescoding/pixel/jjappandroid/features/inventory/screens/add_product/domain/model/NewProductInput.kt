package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.domain.model

import android.net.Uri

data class NewProductInput(
    val itemName: String,
    val itemVariant: String,
    val availableStock: String,
    val onHandStock: String,
    val onTheWayStock: String,
    val itemCostPrice: String,
    val itemSellingPrice: String,
    val itemImageResId: Int,
    val itemUri: Uri?
)
package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation

import android.net.Uri
import androidx.annotation.DrawableRes

data class AddEditProductUiState(
    val itemSku: String = "",
    val itemName: String = "",
    val itemVariant: String = "",
    val availableStock: String = "",
    val onHandStock: String = "",
    val onTheWayStock: String = "",
    val itemCostPrice: String = "",
    val itemSellingPrice: String = "",
    val itemPhotoUri: Uri? = null,
    @DrawableRes val itemPhotoResId: Int = 0,
    val header: String = "",
    val buttonText: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
) {
    val costPriceDisplay: String
        get() = if (itemCostPrice == "0.0") "" else itemCostPrice

    val sellingPriceDisplay: String
        get() = if (itemSellingPrice == "0.0") "" else itemSellingPrice
}
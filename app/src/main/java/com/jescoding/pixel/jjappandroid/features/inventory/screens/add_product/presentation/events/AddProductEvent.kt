package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation.events

import android.net.Uri

sealed class AddProductEvent {
    data class OnPhotoSelected(val uri: Uri?) : AddProductEvent()
    data class OnNameChange(val name: String) : AddProductEvent()
    data class OnVariationChange(val variation: String) : AddProductEvent()
    data class OnCostChange(val cost: String) : AddProductEvent()
    data class OnSellingPriceChange(val price: String) : AddProductEvent()

    data class OnAvailableStockChange(val stock: String) : AddProductEvent()

    data class OnHandStockChange(val stock: String) : AddProductEvent()

    data class OnTheWayStockChange(val stock: String) : AddProductEvent()
}
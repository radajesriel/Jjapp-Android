package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation.events

import android.net.Uri

sealed class AddEditProductEvent {
    data class OnPhotoSelected(val uri: Uri?) : AddEditProductEvent()
    data class OnNameChange(val name: String) : AddEditProductEvent()
    data class OnVariationChange(val variation: String) : AddEditProductEvent()
    data class OnCostChange(val cost: String) : AddEditProductEvent()
    data class OnSellingPriceChange(val price: String) : AddEditProductEvent()
    data class OnAvailableStockChange(val stock: String) : AddEditProductEvent()
    data class OnHandStockChange(val stock: String) : AddEditProductEvent()
    data class OnTheWayStockChange(val stock: String) : AddEditProductEvent()
    object OnSaveProduct : AddEditProductEvent()
}
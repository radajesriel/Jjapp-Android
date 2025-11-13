package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation.events

sealed class AddProductEvent {
    data class OnNameChange(val name: String) : AddProductEvent()
    data class OnVariationChange(val variation: String) : AddProductEvent()
    data class OnCostChange(val cost: String) : AddProductEvent()
    data class OnSellingPriceChange(val price: String) : AddProductEvent()
    data class OnStockChange(
        val availableStock: String,
        val onHandStock: String,
        val onTheWayStock: String
    ) : AddProductEvent()
}
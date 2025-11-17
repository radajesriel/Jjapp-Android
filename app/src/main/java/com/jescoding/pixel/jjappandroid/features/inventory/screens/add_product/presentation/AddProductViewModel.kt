package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation

import androidx.lifecycle.ViewModel
import com.jescoding.pixel.jjappandroid.core.utils.ValidationUtils
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation.events.AddProductEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    fun onEvent(event: AddProductEvent) {
        _uiState.update { currentState ->
            when (event) {
                is AddProductEvent.OnNameChange -> {
                    currentState.copy(itemName = event.name)
                }

                is AddProductEvent.OnVariationChange -> {
                    currentState.copy(itemVariant = event.variation)
                }

                is AddProductEvent.OnCostChange -> {
                    val validatedCost = ValidationUtils.getValidatedCurrency(event.cost)
                    currentState.copy(itemCostPrice = validatedCost)
                }

                is AddProductEvent.OnSellingPriceChange -> {
                    val validatedPrice = ValidationUtils.getValidatedCurrency(event.price)
                    currentState.copy(itemSellingPrice = validatedPrice)
                }

                is AddProductEvent.OnPhotoSelected -> {
                    currentState.copy(itemPhotoUri = event.uri)
                }

                is AddProductEvent.OnAvailableStockChange -> {
                    currentState.copy(availableStock = event.stock)
                }

                is AddProductEvent.OnHandStockChange -> {
                    currentState.copy(onHandStock = event.stock)
                }

                is AddProductEvent.OnTheWayStockChange -> {
                    currentState.copy(onTheWayStock = event.stock)
                }
            }
        }
    }
}
package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.core.domain.dispatcher.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.utils.ValidationUtils
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.domain.model.NewProductInput
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.domain.use_case.SaveProduct
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation.events.AddProductEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val saveProduct: SaveProduct,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    fun updateUiState(update: (AddProductUiState) -> AddProductUiState) {
        _uiState.update(update)
    }

    fun onEvent(event: AddProductEvent) {
        when (event) {
            is AddProductEvent.OnNameChange -> {
                _uiState.update {
                    it.copy(itemName = event.name)
                }
            }

            is AddProductEvent.OnVariationChange -> {
                _uiState.update {
                    it.copy(itemVariant = event.variation)
                }
            }

            is AddProductEvent.OnCostChange -> {
                val validatedCost = ValidationUtils.getValidatedCurrency(event.cost)
                _uiState.update {
                    it.copy(itemCostPrice = validatedCost)
                }
            }

            is AddProductEvent.OnSellingPriceChange -> {
                val validatedPrice = ValidationUtils.getValidatedCurrency(event.price)
                _uiState.update {
                    it.copy(itemSellingPrice = validatedPrice)
                }
            }

            is AddProductEvent.OnPhotoSelected -> {
                _uiState.update {
                    it.copy(itemPhotoUri = event.uri)
                }
            }

            is AddProductEvent.OnAvailableStockChange -> {
                _uiState.update {
                    it.copy(availableStock = event.stock)
                }
            }

            is AddProductEvent.OnHandStockChange -> {
                _uiState.update {
                    it.copy(onHandStock = event.stock)
                }
            }

            is AddProductEvent.OnTheWayStockChange -> {
                _uiState.update {
                    it.copy(onTheWayStock = event.stock)
                }
            }

            is AddProductEvent.OnSaveProduct -> {
                onSaveProduct()
            }
        }
    }

    private fun onSaveProduct() {
        viewModelScope.launch {
            try {
                withContext(dispatcherProvider.io) {
                    val input = NewProductInput(
                        itemName = uiState.value.itemName,
                        itemVariant = uiState.value.itemVariant,
                        availableStock = uiState.value.availableStock,
                        onHandStock = uiState.value.onHandStock,
                        onTheWayStock = uiState.value.onTheWayStock,
                        itemCostPrice = uiState.value.itemCostPrice,
                        itemSellingPrice = uiState.value.itemSellingPrice,
                        itemUri = uiState.value.itemPhotoUri,
                        itemImageResId = R.drawable.round_local_convenience_store_24
                    )
                    saveProduct(input)
                }

                _uiState.update { AddProductUiState(onProductSaved = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unexpected error occured") }
            }
        }

    }
}
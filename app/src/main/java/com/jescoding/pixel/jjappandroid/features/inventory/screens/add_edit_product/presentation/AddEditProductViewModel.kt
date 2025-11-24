package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider
import com.jescoding.pixel.jjappandroid.core.utils.ValidationUtils
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.model.NewProductInput
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.LoadProduct
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.SaveProduct
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation.events.AddEditProductEvent
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation.events.AddEditProductSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    private val saveProduct: SaveProduct,
    private val loadProduct: LoadProduct,
    private val dispatcherProvider: DispatcherProvider,
    private val resourceProvider: ResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemSku: String? = savedStateHandle["itemSku"]
    private val _uiState = MutableStateFlow(AddEditProductUiState())
    val uiState: StateFlow<AddEditProductUiState> = _uiState.asStateFlow()

    private val _sideEffectFlow = MutableSharedFlow<AddEditProductSideEffect>()
    val sideEffectFlow = _sideEffectFlow.asSharedFlow()

    init {
        if (itemSku != null) {
            // This is the "Edit" flow
            _uiState.update {
                it.copy(
                    header = resourceProvider.getString(R.string.text_label_edit_product),
                    buttonText = resourceProvider.getString(R.string.button_label_update_product)
                )
            }
            onLoadProduct(itemSku)
        } else {
            // This is the "Add" flow
            _uiState.update {
                it.copy(
                    header = resourceProvider.getString(R.string.text_label_add_product),
                    buttonText = resourceProvider.getString(R.string.button_label_save_product)
                )
            }
        }
    }

    fun updateUiState(update: (AddEditProductUiState) -> AddEditProductUiState) {
        _uiState.update(update)
    }

    fun onEvent(event: AddEditProductEvent) {
        when (event) {
            is AddEditProductEvent.OnNameChange -> {
                _uiState.update {
                    it.copy(itemName = event.name)
                }
            }

            is AddEditProductEvent.OnVariationChange -> {
                _uiState.update {
                    it.copy(itemVariant = event.variation)
                }
            }

            is AddEditProductEvent.OnCostChange -> {
                val validatedCost = ValidationUtils.getValidatedCurrency(event.cost)
                _uiState.update {
                    it.copy(itemCostPrice = validatedCost)
                }
            }

            is AddEditProductEvent.OnSellingPriceChange -> {
                val validatedPrice = ValidationUtils.getValidatedCurrency(event.price)
                _uiState.update {
                    it.copy(itemSellingPrice = validatedPrice)
                }
            }

            is AddEditProductEvent.OnPhotoSelected -> {
                _uiState.update {
                    it.copy(itemPhotoUri = event.uri)
                }
            }

            is AddEditProductEvent.OnAvailableStockChange -> {
                _uiState.update {
                    it.copy(availableStock = event.stock)
                }
            }

            is AddEditProductEvent.OnHandStockChange -> {
                _uiState.update {
                    it.copy(onHandStock = event.stock)
                }
            }

            is AddEditProductEvent.OnTheWayStockChange -> {
                _uiState.update {
                    it.copy(onTheWayStock = event.stock)
                }
            }

            is AddEditProductEvent.OnSaveProduct -> {
                onSaveProduct()
            }
        }
    }

    private fun onLoadProduct(itemSku: String) {
        viewModelScope.launch {
            loadProduct(itemSku)
                .flowOn(dispatcherProvider.io)
                .collect { item ->
                    item?.let {
                        _uiState.update {
                            it.copy(
                                itemSku = item.itemSku,
                                itemName = item.itemName,
                                itemVariant = item.itemVariant,
                                availableStock = item.availableStock.toString(),
                                onHandStock = item.onHandStock.toString(),
                                onTheWayStock = item.onTheWayStock.toString(),
                                itemCostPrice = item.itemCostPrice.toString(),
                                itemSellingPrice = item.itemSellingPrice.toString(),
                                itemPhotoUri = item.itemUri,
                                itemPhotoResId = item.itemImageResId
                            )
                        }
                    }
                }
        }
    }


    private fun onSaveProduct() {
        viewModelScope.launch {
            try {
                val input = NewProductInput(
                    itemSku = itemSku,
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
                withContext(dispatcherProvider.io) {
                    saveProduct(input)
                }
                _sideEffectFlow.emit(AddEditProductSideEffect.NavigateOnSave)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unexpected error occurred") }
            }
        }
    }
}
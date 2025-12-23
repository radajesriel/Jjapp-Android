package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider
import com.jescoding.pixel.jjappandroid.core.utils.ValidationUtils
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.model.NewProductInput
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.DeleteProduct
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    private val saveProduct: SaveProduct,
    private val loadProduct: LoadProduct,
    private val deleteProduct: DeleteProduct,
    private val resourceProvider: ResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemSku: String? = savedStateHandle["itemSku"]
    private val _uiState = MutableStateFlow(AddEditProductUiState())
    val uiState: StateFlow<AddEditProductUiState> = _uiState.asStateFlow()

    private val _sideEffectFlow = MutableSharedFlow<AddEditProductSideEffect>()
    val sideEffectFlow = _sideEffectFlow.asSharedFlow()

    init {
       initializeScreen()
    }

    private fun initializeScreen() {
        if (itemSku != null) {
            _uiState.update {
                it.copy(
                    header = resourceProvider.getString(R.string.text_label_edit_product),
                    buttonLabel = resourceProvider.getString(R.string.button_label_update_product)
                )
            }
            onEvent(AddEditProductEvent.OnLoadProduct(itemSku))
        } else {
            _uiState.update {
                it.copy(
                    header = resourceProvider.getString(R.string.text_label_add_product),
                    buttonLabel = resourceProvider.getString(R.string.button_label_save_product)
                )
            }
        }
    }

    fun onEvent(event: AddEditProductEvent) {
        when (event) {
            is AddEditProductEvent.OnNameChange -> _uiState.update { it.copy(itemName = event.name) }
            is AddEditProductEvent.OnVariationChange -> _uiState.update { it.copy(itemVariant = event.variation) }
            is AddEditProductEvent.OnPhotoSelected -> _uiState.update { it.copy(itemPhotoUri = event.uri) }
            is AddEditProductEvent.OnAvailableStockChange -> _uiState.update {
                it.copy(
                    availableStock = event.stock
                )
            }

            is AddEditProductEvent.OnHandStockChange -> _uiState.update { it.copy(onHandStock = event.stock) }
            is AddEditProductEvent.OnTheWayStockChange -> _uiState.update { it.copy(onTheWayStock = event.stock) }
            is AddEditProductEvent.OnSaveProduct -> onSaveProduct()

            is AddEditProductEvent.OnCostChange -> {
                _uiState.update { it.copy(itemCostPrice = ValidationUtils.getValidatedCurrency(event.cost)) }
            }

            is AddEditProductEvent.OnSellingPriceChange -> {
                _uiState.update {
                    it.copy(
                        itemSellingPrice = ValidationUtils.getValidatedCurrency(
                            event.price
                        )
                    )
                }
            }

            is AddEditProductEvent.OnDeleteProduct -> onDeleteProduct()
            is AddEditProductEvent.OnLoadProduct -> onLoadProduct(event.itemSku)
        }
    }

    private fun onLoadProduct(itemSku: String) = viewModelScope.launch {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

        try {
            loadProduct(itemSku)?.let { item ->
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
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Unexpected error occurred") }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun onDeleteProduct() = viewModelScope.launch {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

        itemSku?.let {
            try {
                deleteProduct(itemSku)
                _sideEffectFlow.emit(AddEditProductSideEffect.NavigateOnDelete)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Unexpected error occurred") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onSaveProduct() = viewModelScope.launch {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

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

        try {
            saveProduct(input)
            _sideEffectFlow.emit(AddEditProductSideEffect.NavigateOnSave)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Unexpected error occurred") }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
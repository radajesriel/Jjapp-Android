package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation

import android.net.Uri
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _itemSku = MutableStateFlow("")
    private val _itemName = MutableStateFlow("")
    private val _itemVariant = MutableStateFlow("")
    private val _availableStock = MutableStateFlow("")
    private val _onHandStock = MutableStateFlow("")
    private val _onTheWayStock = MutableStateFlow("")
    private val _itemCostPrice = MutableStateFlow("")
    private val _itemSellingPrice = MutableStateFlow("")
    private val _itemPhotoUri = MutableStateFlow<Uri?>(null)
    private val _itemPhotoResId = MutableStateFlow(0)
    private val _header = MutableStateFlow("")
    private val _buttonLabel = MutableStateFlow("")
    private val _error = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val itemSku: String? = savedStateHandle["itemSku"]
    private val _uiState = MutableStateFlow(AddEditProductUiState())
    val uiState: StateFlow<AddEditProductUiState> = _uiState.asStateFlow()

    private val _sideEffectFlow = MutableSharedFlow<AddEditProductSideEffect>()
    val sideEffectFlow = _sideEffectFlow.asSharedFlow()

    init {
        observeStateChanges()
        initializeScreen()
    }

    private fun initializeScreen() {
        if (itemSku != null) {
            _header.value = resourceProvider.getString(R.string.text_label_edit_product)
            _buttonLabel.value = resourceProvider.getString(R.string.button_label_update_product)
            onLoadProduct(itemSku)
        } else {
            _header.value = resourceProvider.getString(R.string.text_label_add_product)
            _buttonLabel.value = resourceProvider.getString(R.string.button_label_save_product)
        }
    }

    private fun observeStateChanges() {
        combine(
            _itemSku, _itemName, _itemVariant, _availableStock,
            _onHandStock, _onTheWayStock, _itemCostPrice, _itemSellingPrice,
            _itemPhotoUri, _itemPhotoResId, _header, _buttonLabel,
            _isLoading, _error
        ) { values ->
            AddEditProductUiState(
                itemSku = values[0] as String,
                itemName = values[1] as String,
                itemVariant = values[2] as String,
                availableStock = values[3] as String,
                onHandStock = values[4] as String,
                onTheWayStock = values[5] as String,
                itemCostPrice = values[6] as String,
                itemSellingPrice = values[7] as String,
                itemPhotoUri = values[8] as Uri?,
                itemPhotoResId = values[9] as Int,
                header = values[10] as String,
                buttonLabel = values[11] as String,
                isLoading = values[12] as Boolean,
                error = values[13] as String?
            )
        }.onEach {
            _uiState.value = it
        }.launchIn(viewModelScope)

    }

    fun onEvent(event: AddEditProductEvent) {
        when (event) {
            is AddEditProductEvent.OnNameChange -> _itemName.value = event.name
            is AddEditProductEvent.OnVariationChange -> _itemVariant.value = event.variation
            is AddEditProductEvent.OnPhotoSelected -> _itemPhotoUri.value = event.uri
            is AddEditProductEvent.OnAvailableStockChange -> _availableStock.value = event.stock
            is AddEditProductEvent.OnHandStockChange -> _onHandStock.value = event.stock
            is AddEditProductEvent.OnTheWayStockChange -> _onTheWayStock.value = event.stock
            is AddEditProductEvent.OnSaveProduct -> onSaveProduct()

            is AddEditProductEvent.OnCostChange -> {
                _itemCostPrice.value = ValidationUtils.getValidatedCurrency(event.cost)
            }

            is AddEditProductEvent.OnSellingPriceChange -> {
                _itemSellingPrice.value = ValidationUtils.getValidatedCurrency(event.price)
            }

            is AddEditProductEvent.OnDeleteProduct -> onDeleteProduct()
        }
    }

    private fun onLoadProduct(itemSku: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        try {
            loadProduct(itemSku)?.let { item ->
                _itemSku.value = item.itemSku
                _itemName.value = item.itemName
                _itemVariant.value = item.itemVariant
                _availableStock.value = item.availableStock.toString()
                _onHandStock.value = item.onHandStock.toString()
                _onTheWayStock.value = item.onTheWayStock.toString()
                _itemCostPrice.value = item.itemCostPrice.toString()
                _itemSellingPrice.value = item.itemSellingPrice.toString()
                _itemPhotoUri.value = item.itemUri
                _itemPhotoResId.value = item.itemImageResId
            }
        } catch (e: Exception) {
            _error.value = "Unexpected error occurred"
        } finally {
            _isLoading.value = false
        }
    }

    private fun onDeleteProduct() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        itemSku?.let {
            try {
                deleteProduct(itemSku)
                _sideEffectFlow.emit(AddEditProductSideEffect.NavigateOnDelete)
            } catch (e: Exception) {
                _error.value = "Unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun onSaveProduct() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

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
            _error.value = "Unexpected error occurred"
        } finally {
            _isLoading.value = false
        }
    }
}